package com.lloydsbyte.careeradvr_ai.proSpecific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.analytics.Analytix
import com.lloydsbyte.careeradvr_ai.bottomsheets.MoreInfoBottomSheet
import com.lloydsbyte.careeradvr_ai.chat.ChatFragment
import com.lloydsbyte.careeradvr_ai.databinding.FragmentProSpecificBinding
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.core.assetloader.AssetLoader
import com.lloydsbyte.network.ConfigModel
import timber.log.Timber

class ProSpecificFragment : Fragment() {
    companion object {
        val PERSONAL_BOOL_KEY: String = "PERSONAL"
    }
    lateinit var binding: FragmentProSpecificBinding
    private lateinit var proAdapter: ProSpecificAdapter
    private var promptsList: List<ConfigModel.Prompt> = emptyList()
    private var animate: Boolean = true
    private var personalPrompts: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProSpecificBinding.inflate(inflater, container, false)
        proAdapter = ProSpecificAdapter()
        personalPrompts = arguments?.getBoolean(PERSONAL_BOOL_KEY)?:true
        promptsList =  (requireActivity() as MainActivity).getListOfPrompts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            proBackFab.setOnClickListener {
                findNavController().popBackStack()
            }
            proRecyclerview.apply {
                adapter = proAdapter
                layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)
                proAdapter.initAdapter(savedInstanceState== null, this, promptsList)
                proAdapter.onItemClicked = {
                    Analytix().reportProUsed(requireActivity().applicationContext, it.title)
                    val bundle = Bundle()
                    bundle.putString(ChatFragment.PROMPT_TITLE, it.title)
                    bundle.putString(ChatFragment.PROMPT_KEY,prepPrompt(it.title, it.systemPrompt))
                    bundle.putString(ChatFragment.PROMPT_INSTRUCTIONS, it.instructions)
                    findNavController().navigate(R.id.action_proFragment_to_chatFragment, bundle)
                }
                proAdapter.onItemLongClicked = {
                    val description: String = if (it.description.isEmpty())"To be determined soon" else it.description
                    val bottomsheet = MoreInfoBottomSheet.createInstance(it.title,description)
                    bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
                }
                Timber.d("JL_ professions is ${promptsList.size} full")
                if (proAdapter.adapterItems.isEmpty()) loadAdapter()
            }
        }
    }

    private fun prepPrompt(title: String, prompt: String): String {
        //This function will combine the users name, the default prompt and the additional prompt
        val defaultPrompt = (requireActivity() as MainActivity).getDefaultPrompt()
        return Gpt_Helper().promptCreator(requireActivity(), defaultPrompt, title, prompt)
    }
    private fun loadAdapter() {
        if (promptsList.isEmpty()) {
            //Something went wrong, pull config file again
//            promptsList = (requireActivity() as MainActivity).getListOfPrompts(personalPrompts)
            proAdapter.initAdapter(animate, binding.proRecyclerview, promptsList)
            animate = false
        } else {
            proAdapter.initAdapter(animate, binding.proRecyclerview, promptsList)
            animate = false
        }
    }


}