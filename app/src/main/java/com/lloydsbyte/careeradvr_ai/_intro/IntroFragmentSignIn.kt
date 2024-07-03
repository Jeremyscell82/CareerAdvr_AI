package com.lloydsbyte.careeradvr_ai._intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lloydsbyte.careeradvr_ai.databinding.FragmentIntroSignInBinding
import com.lloydsbyte.core.utilz.Utilz


//This is the final screen to my intro section, it does nothing but show a view and listen for a click
class IntroFragmentSignIn : Fragment() {

    lateinit var binding: FragmentIntroSignInBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            landingNextFab.setOnClickListener {
                (requireActivity() as IntroSectionActivity).launchSignIn()
            }
            landingSkipFab.visibility = if (Utilz.isInDebugMode(requireActivity())) View.VISIBLE else View.GONE
            landingSkipFab.setOnClickListener {
                (requireActivity() as IntroSectionActivity).initiateApp()
            }
        }

    }
}