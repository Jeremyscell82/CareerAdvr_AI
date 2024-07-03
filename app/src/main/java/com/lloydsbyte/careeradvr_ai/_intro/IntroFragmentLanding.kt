package com.lloydsbyte.careeradvr_ai._intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.core.utilz.UtilzViewHelper.Companion.fadeIn
import com.lloydsbyte.careeradvr_ai.databinding.FragmentIntroLandingBinding

//This is the first fragment to display for the intro section.
//You can modify all the content from the strings file or the layout file
class IntroFragmentLanding: Fragment() {

    lateinit var binding: FragmentIntroLandingBinding
    var showAnimations: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroLandingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Show animations only on first draw
        if (showAnimations){
            animateUI()
        } else {
            binding.apply {
                landingLottieImages.visibility = View.VISIBLE
                introNextFab.visibility = View.VISIBLE
            }
        }
        binding.introNextFab.setOnClickListener {
            findNavController().navigate(R.id.action_intro_to_terms)
        }

    }

    private fun  animateUI(){
        binding.apply {
            landingLottieImages.fadeIn(1600)
            introNextFab.fadeIn(2400)
        }
    }



    override fun onPause() {
        super.onPause()
        showAnimations = false
    }
}