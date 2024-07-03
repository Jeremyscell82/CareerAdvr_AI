package com.lloydsbyte.careeradvr_ai._intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.core.utilz.UtilzViewHelper.Companion.fadeIn
import com.lloydsbyte.core.utilz.UtilzViewHelper.Companion.fadeOut
import com.lloydsbyte.careeradvr_ai.databinding.FragmentIntroTermsBinding
import com.lloydsbyte.core.utilz.Utilz

//Just a fragment for me to keep track of where people are finding my application. Feel free to use everything in this fragment
class IntroFragmentTerms: Fragment() {

    lateinit var binding: FragmentIntroTermsBinding
    var showAnimations: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroTermsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            termsDescription.fadeIn(1400)
            termsLink.fadeIn(2200)
            termsLink.setOnClickListener {
                Utilz.openInBrowser(requireActivity(), "https://openai.com/policies")
            }
            termsChecked.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    termsNextFab.fadeIn(1000)
                } else {
                    termsNextFab.fadeOut()
                }
            }
            termsNextFab.setOnClickListener {
                findNavController().navigate(R.id.action_terms_to_profile)
            }
        }
    }

}