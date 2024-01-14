package com.lloydsbyte.careeradvr_ai._intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lloydsbyte.careeradvr_ai.databinding.FragmentIntroNewuserBinding

class IntroNewUserFragment: Fragment() {


    lateinit var binding: FragmentIntroNewuserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroNewuserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            newuserLaunchFab.setOnClickListener {
                (requireActivity() as IntroSectionActivity).initiateApp()
            }
        }
    }
}