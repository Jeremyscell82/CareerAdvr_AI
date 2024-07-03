package com.lloydsbyte.careeradvr_ai.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.analytics.Analytix
import com.lloydsbyte.careeradvr_ai.databinding.FragmentChatBinding
import com.lloydsbyte.careeradvr_ai.utilz.GptTokenController
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.careeradvr_ai.utilz.UserProfileHelper
import com.lloydsbyte.chap_e.chat.menu.MenuDialogFragment
import com.lloydsbyte.chap_e.chat.menu.MenuInterface
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.core.custombottomsheet.BottomsheetEditFieldFragment
import com.lloydsbyte.core.custombottomsheet.ConfirmInterface
import com.lloydsbyte.core.custombottomsheet.ErrorBottomsheet
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.search_bottomsheet.BottomsheetSearchInterface
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.core.utilz.UtilzSendItHelper
import com.lloydsbyte.core.utilz.UtilzViewHelper
import com.lloydsbyte.database.DatabaseController
import com.lloydsbyte.database.DatabaseInterface
import com.lloydsbyte.database.models.ChatHeaderModel
import com.lloydsbyte.network.NetworkClient
import com.lloydsbyte.network.NetworkConstants
import com.lloydsbyte.network.NetworkController
import com.lloydsbyte.network.interfaces.GptQuestionInterface
import com.lloydsbyte.network.responses.ChatGptResponse

class ChatFragment: Fragment(), GptQuestionInterface, MenuInterface, BottomsheetSearchInterface,
    DatabaseInterface, ConfirmInterface {

    companion object {
        val PROMPT_TITLE: String = "PROMPT_TITLE"
        val PROMPT_KEY: String = "PROMPT_KEY"
        val PROMPT_INSTRUCTIONS = "PROMPT_INTSRUCTIONS"
        val PROMPT_IS_INTERVIEW_MOCK = "PROMPT_INTERVIEW_MOCK"
    }


    lateinit var binding: FragmentChatBinding
    lateinit var viewModel: ChatViewModel
    lateinit var chatAdapter: ChatAdapter
    lateinit var adHelper: GptTokenController

    //Network Call Variables
    private val networkClient by lazy {
        NetworkClient.create(NetworkConstants.gptBaseUrl)
    }
    private val networkController by lazy {
        NetworkController(networkClient)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        chatAdapter = ChatAdapter()
        adHelper = GptTokenController()
        viewModel.chatTitle = arguments?.getString(PROMPT_TITLE)?:getString(R.string.chat_basic_title)
        viewModel.systemPrompt = arguments?.getString(PROMPT_KEY)?:getString(R.string.chat_default_prompt)
        viewModel.instructions = arguments?.getString(PROMPT_INSTRUCTIONS)?:""
        viewModel.isMockInterview = arguments?.getBoolean(PROMPT_IS_INTERVIEW_MOCK)?:false
        viewModel.isUserSubscribed = UserProfileHelper.isUserSubscribed(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            chatBackFab.setOnClickListener {
                findNavController().popBackStack()
            }
            chatMenuFab.setOnClickListener {
                //Show bottomsheet with menu items,
                val menuDialog = MenuDialogFragment(this@ChatFragment)
                menuDialog.show(requireActivity().supportFragmentManager, menuDialog.tag)
            }
            if (viewModel.chatThread.isEmpty())chatMenuFab.hide()
            chatTitle.text = viewModel.chatTitle
            chatInputField.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        viewModel.questionEntered = s.toString()
                        chatSendFab.isEnabled = s.toString().isNotEmpty()
                    }

                })
            }
            chatConfigFab.setOnClickListener {
                showInstructions()
            }

            chatSendFab.setOnClickListener {
                //In the event they reload and want to fire off one question, prevent it
                if (!GptTokenController().hasReachedMax(requireActivity(), viewModel.convoCost, viewModel.isUserSubscribed)){
                    askQuestion()
                } else {
                    maxLimitReached()
                }
            }
            chatRecyclerview.apply {
                adapter = chatAdapter
                if (viewModel.chatThread.isNotEmpty())chatAdapter.populateChat(true, chatRecyclerview, viewModel.chatThread)
            }

        }
        //Show keyboard and set focus
        UtilzViewHelper.showSoftKeyboard(requireActivity(), binding.chatInputField)
        if (viewModel.instructions.isNotEmpty()) {
            if (viewModel.showInstructions)showInstructions()
        } else {
            //No instructions to show
            binding.chatConfigFab.hide()
        }

        //Todo Initi Data Feeds if
    }

    private fun showInstructions() {
        viewModel.showInstructions = false
        val bottomsheet = ErrorBottomsheet.createInstance(
            errorTitle = "Instructions",
            errorMessage = viewModel.instructions
        )
        bottomsheet.show(requireActivity().supportFragmentManager ,bottomsheet.tag)
    }

    private fun askQuestion() {


        //Create System Prompt
        val systemPrompt = Gpt_Helper().createSystemPrompt(
            viewModel.getConvoTimeStampId(),
            viewModel.systemPrompt
        )
        //Create Chat Model
        val chatModel = Gpt_Helper().createChatModel(
            viewModel.getConvoTimeStampId(),
            "user",
            viewModel.questionEntered
        )

        //Add Chat Model to the conversation
        viewModel.chatThread.add(chatModel)

        dropKeyboard()

        (requireActivity() as MainActivity).showLoadingView(true)
        val model = if (StoredPref(requireActivity()).useGpt4Turbo())NetworkConstants.gpt_4_turbo else NetworkConstants.gpt_4
        val jsonPayload = Gpt_Helper().prepPayload(requireActivity(), model, viewModel.chatThread, systemPrompt)
        val key = "${NetworkConstants.ai_key}${(requireActivity() as MainActivity).getSecretKey()}"
        networkController.chatGptRequest(
            this, key, jsonPayload
        )

        //Update UI
        binding.chatInputField.setText("")
        chatAdapter.addChat(binding.chatRecyclerview, chatModel)
        binding.chatRecyclerview.scrollToPosition(viewModel.chatThread.size-1)

        //Checks based off current convoCost
        if (adHelper.shouldShowAds( viewModel.convoCost)){
            //Show ad
            if(!UserProfileHelper.isUserSubscribed(requireActivity()))(requireActivity() as MainActivity).fireOffAd()
        }
    }

    private fun dropKeyboard() {
        UtilzViewHelper.hideSoftKeyboard(requireActivity(), binding.chatInputField)
        binding.chatInputField.clearFocus()
    }

    override fun onNetworkComplete(result: ChatGptResponse.GptData) {
        (requireActivity() as MainActivity).showLoadingView(false)


        //Update viewModel
        viewModel.apply {
            gptConvoId = result.id
            convoCost = result.usage.totalTokens
        }

        //Update UI
        binding.chatCost.text = resources.getString(R.string.chat_convo_cost, viewModel.convoCost.toString())
        val chatModel = Gpt_Helper().createChatModel(viewModel.getConvoTimeStampId(), result.choices.first().message.role, result.choices.first().message.content)
        chatAdapter.addChat(binding.chatRecyclerview, chatModel)
        binding.chatRecyclerview.scrollToPosition(viewModel.chatThread.size)
        viewModel.chatThread.add(chatModel)
        binding.chatMenuFab.show()

        //Check if limit has been reached, if in mock interview override to allow the user to finish.
        if (hasHitLimit(viewModel.isMockInterview)){
            maxLimitReached()
        }
    }

    override fun onNetworkError(error: Throwable) {
        ErrorController.logError(error)
        showErrorBottomsheet("Network Error", error.message.toString())
        (requireActivity() as MainActivity).showLoadingView(false)
        binding.chatInputSectionLayout.visibility = View.GONE
    }

    private fun hasHitLimit(override: Boolean): Boolean {
        return if (!override)GptTokenController().hasReachedMax(requireActivity(), viewModel.convoCost, viewModel.isUserSubscribed) else false
    }
    private fun maxLimitReached() {
        //Remove input field layout
        binding.chatInputSectionLayout.visibility = View.GONE

        //Display dialog
        CustomDialogs.launchDialog(
            sfm = requireActivity().supportFragmentManager,
            title = resources.getString(R.string.error_max_reached_title),
            message = resources.getString(R.string.error_max_reached_message),
            posText = resources.getString(R.string.dialog_ok),
            negText = null,
            okRun = null,
            cancelRun = null,
            cancelable = true
        )
    }

    private fun showErrorBottomsheet(errorTitle: String, errorMsg: String) {
        val bottomsheet = ErrorBottomsheet.createInstance(errorTitle, errorMsg)
        bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
    }

    private fun saveConversation(saveName: String) {
        viewModel.convoHeaderModel = ChatHeaderModel(
            dbKey = 0,
            chatGptConvoId = viewModel.gptConvoId,
            convoName = saveName,
            conversationID = viewModel.getConvoTimeStampId(),
            viewModel.chatTitle
        )
        saveToDatabase()
    }

    private fun saveToDatabase() {
        DatabaseController(this).addConversation(requireActivity().applicationContext, viewModel.chatThread, viewModel.convoHeaderModel!!)
    }
    override fun onPhraseEntered(query: String) {
        //Save conversation
        saveConversation(query)
    }

    override fun onCanceled() {
    }

    override fun onComplete() {
        //Dismiss the conversation or keep it alive???
        CustomDialogs.snackbar(binding.root, "Conversation saved")
    }

    override fun onDestroy() {
        networkController.destroyConnection()
        GptTokenController().addToTotalCount(requireActivity(), viewModel.convoCost)
        Analytix().reportCost(requireActivity().applicationContext, viewModel.convoCost.toString(), adHelper.adsShown().toString())
        super.onDestroy()
    }

    override fun onActionPressed() {
    }

    override fun onSave() {
        if (viewModel.convoHeaderModel == null){
            //Show create name bottomsheet
            //Display Bottomsheet to get name of conversation
            val bottomsheet = BottomsheetEditFieldFragment.createInstance(
                hint = "Pick a name to save the conversation under",
                phrase = "",
                number = false,
                this@ChatFragment
            )
            bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
        } else {
            //Update conversation
            saveToDatabase()
        }
    }

    override fun onShare() {
        val sharableChat =  Gpt_Helper().createSharableConversation(viewModel.chatThread)
        UtilzSendItHelper().sendChat(requireActivity(), sharableChat)
    }

    override fun onReport() {
        UtilzSendItHelper().contactDeveloperWithUID(
            requireActivity(),
            Utilz.getVersionName(requireActivity()),
            FirebaseAuth.getInstance().currentUser?.uid ?: "NA"
        )
    }

}