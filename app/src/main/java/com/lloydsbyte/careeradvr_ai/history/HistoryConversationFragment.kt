package com.lloydsbyte.careeradvr_ai.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lloydsbyte.careeradvr_ai.chat.ChatAdapter
import com.lloydsbyte.careeradvr_ai.databinding.FragmentHistoryConversationBinding
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.core.utilz.UtilzSendItHelper
import com.lloydsbyte.database.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class HistoryConversationFragment : Fragment() {
    companion object {
        val CONVO_ID = "convo_id"
    }

    lateinit var binding: FragmentHistoryConversationBinding
    private lateinit var viewModel: HistoryViewModel
    private var databaseConnection = CompositeDisposable()
    private lateinit var chatAdapter: ChatAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryConversationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        viewModel.convoHeaderModel = arguments?.getParcelable(CONVO_ID)
        chatAdapter = ChatAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.convoHeaderModel == null) {
            findNavController().popBackStack()
            Toast.makeText(
                requireActivity(),
                "Oops, something went wrong. Try again",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.apply {
            historyBackFab.setOnClickListener {
                findNavController().popBackStack()
            }
            historyConvoRecyclerview.apply {
                adapter = chatAdapter
            }
            historyShareFab.setOnClickListener {
                UtilzSendItHelper().sendChat(requireActivity(), Gpt_Helper().createSharableConversation(viewModel.convoCarbonCopy))
            }
            viewModel.getConversation(
                AppDatabase.getDatabase(requireActivity().applicationContext),
                viewModel.convoHeaderModel!!.conversationID
            )
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({conversation ->
                    viewModel.convoCarbonCopy = conversation
                    chatAdapter.populateChat(true, historyConvoRecyclerview, conversation)
                },
                    {error ->

                    }).addTo(databaseConnection)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseConnection.dispose()
        viewModel.convoHeaderModel = null
    }
}