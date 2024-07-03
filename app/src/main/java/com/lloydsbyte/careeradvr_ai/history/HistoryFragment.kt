package com.lloydsbyte.careeradvr_ai.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lloydsbyte.core.search_bottomsheet.BottomsheetSearchInterface
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.FragmentHistoryBinding
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.core.custombottomsheet.BottomsheetConfirm
import com.lloydsbyte.core.custombottomsheet.ConfirmInterface
import com.lloydsbyte.core.search_bottomsheet.BottomsheetSearchFragment
import com.lloydsbyte.database.AppDatabase
import com.lloydsbyte.database.DatabaseController
import com.lloydsbyte.database.DatabaseInterface
import com.lloydsbyte.database.models.ChatHeaderModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class HistoryFragment : Fragment(), BottomsheetSearchInterface, ConfirmInterface,
    DatabaseInterface {

    lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModel: HistoryViewModel
    private var databaseConnection = CompositeDisposable()
    private var deletableHeader: ChatHeaderModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        historyAdapter = HistoryAdapter()
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            historyBackFab.setOnClickListener {
                findNavController().popBackStack()
            }
            historySearchFab.setOnClickListener {
                val bottomsheet = BottomsheetSearchFragment.createInstance("Search chat history", this@HistoryFragment)
                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
            }
            historyClearSearchFab.setOnClickListener {
                //Clear search
                historyAdapter.searchItems("", historyRecyclerview)
                historyClearSearchFab.visibility = View.GONE
            }

            historyRecyclerview.apply {
                layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                adapter = historyAdapter
                historyAdapter.onItemClicked = {
                    val bundle = Bundle()
                    bundle.putParcelable(HistoryConversationFragment.CONVO_ID, it)
                    findNavController().navigate(
                        R.id.action_historyFragment_to_historyConvoFragment,
                        bundle
                    )
                }
                historyAdapter.onLongItemClicked = {
                    deletableHeader = it
                    var bottomsheet = BottomsheetConfirm.createInstance(this@HistoryFragment, resources.getString(R.string.history_delete_title), resources.getString(R.string.history_delete_message, it.convoName), resources.getString(R.string.history_delete))
                    bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
                }
                if (historyAdapter.isAdapterPopulated())historySearchFab.show() else historySearchFab.hide()
            }
            viewModel.getChatHistory(AppDatabase.getDatabase(requireActivity().applicationContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { conversations ->
                        historyAdapter.initAdapter(conversations, historyRecyclerview)
                    },
                    { error ->
                        ErrorController.logError(error.message)
                    }
                ).addTo(databaseConnection)

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        databaseConnection.dispose()
    }

    override fun onPhraseEntered(query: String) {
        historyAdapter.searchItems(query, binding.historyRecyclerview)
        binding.historyClearSearchFab.visibility = View.VISIBLE
    }

    override fun onCanceled() {

    }

    override fun onActionPressed() {
        if (deletableHeader!=null){
            DatabaseController(this).removeConversation(requireActivity().applicationContext, deletableHeader!!)
        }
    }

    override fun onComplete() {

    }
}