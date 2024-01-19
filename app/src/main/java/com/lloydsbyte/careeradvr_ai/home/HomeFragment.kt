package com.lloydsbyte.careeradvr_ai.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.analytics.MixPanelController
import com.lloydsbyte.careeradvr_ai.chat.ChatFragment
import com.lloydsbyte.careeradvr_ai.databinding.FragmentHomeBinding
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.network.ConfigModel
import timber.log.Timber

/**
 * This fragment will hold the profile screen, basically the portfolio screen
 */
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var homeAdapter: HomeAdapter
    var homePrompts: List<ConfigModel.Prompt> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeAdapter = HomeAdapter()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            homeSettingsFab.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            }

            homeWelcomeText.text = resources.getString(
                R.string.home_welcome_text,
                StoredPref(requireActivity()).getUserName()
            )

            homeStartAmaChatCardview.setOnClickListener {
                MixPanelController().reportChatUsed(requireActivity(), MixPanelController.EVENT_USED_AMA)
                val bundle = Bundle()
                val defaultPrompt =  Gpt_Helper().promptCreator(requireActivity(), (requireActivity() as MainActivity).getDefaultPrompt(), null, null)
                bundle.putString(ChatFragment.PROMPT_KEY,defaultPrompt)
                findNavController().navigate(R.id.action_homeFragment_to_chatFragment, bundle)
            }

            homeViewSavedChatsCardview.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
            }

            homeProCardview.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_proFragment)
            }


//            homeCategoryRecyclerview.apply {
//                adapter = homeAdapter
//                Timber.d("JL_ home prompts count: ${homePrompts.size}")
//                homeAdapter.initAdapter(true, binding.homeCategoryRecyclerview, homePrompts)
//                layoutManager = GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
//                homeAdapter.onItemClicked = {
//                    val bundle = Bundle()
//                    bundle.putString(ChatFragment.PROMPT_TITLE, it.title)
//                    bundle.putString(ChatFragment.PROMPT_KEY,prepPrompt(it.title, it.systemPrompt))
//                    findNavController().navigate(R.id.action_homeFragment_to_chatFragment, bundle)
//                }
//                homeAdapter.onItemLongClicked = {
//                    val description: String = it.description.ifEmpty { "To be determined soon" }
//                    val bottomsheet = MoreInfoBottomSheet.createInstance(it.title,description)
//                    bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
//                }
//
//            }

//
//
//            //Update User Feeds
//            UserProfileHelper.createProfileReference(UserProfileHelper.endpointStatus).addValueEventListener(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()){
//                        val status: String = snapshot.value.toString()
//                        StoredPref(requireActivity()).setProfileStatus(status)
//                    } else {
//                        UserProfileHelper.updateUserStatus(UserProfileHelper.statusFree)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    ErrorController.logError("DB status cancelled: ${error.toException()}")
//                }
//
//            })
//
//            UserProfileHelper.createProfileReference(UserProfileHelper.endpointDateLimit).addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()){
//                        val date: String = snapshot.value.toString()
//                        StoredPref(requireActivity()).setUsageDate(date.toLong())
//                    } else {
//                        UserProfileHelper.updateDateLimit(UtilzDateHelper(UtilzDateHelper.DF_CAL).buildMillisDate())
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    ErrorController.logError("DB Date cancelled: ${error.toException()}")
//                }
//
//            })
//
//            UserProfileHelper.createProfileReference(UserProfileHelper.endpointUsageLimit).addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()){
//                        val limit: String = snapshot.value.toString()
//                        StoredPref(requireActivity()).setUsageLimit(limit.toInt())
//                    } else {
//                        UserProfileHelper.updateUsageLimit(0)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    ErrorController.logError("DB usage cancelled: ${error.toException()}")
//                }
//
//            })
        }
    }

    private fun prepPrompt(title: String, prompt: String): String {
        //This function will combine the users name, the default prompt and the additional prompt
        val defaultPrompt = (requireActivity() as MainActivity).getDefaultPrompt()
        return Gpt_Helper().promptCreator(requireActivity(), defaultPrompt, title, prompt)
    }
}