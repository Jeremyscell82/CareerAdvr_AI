package com.lloydsbyte.careeradvr_ai._intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lloydsbyte.careeradvr_ai.databinding.FragmentIntroPromoBinding
import com.lloydsbyte.careeradvr_ai.utilz.UserProfileHelper
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.Utilz

class IntroPromoFragment: Fragment() {

    lateinit var binding: FragmentIntroPromoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroPromoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val storeConfigFile = StoredPref(requireActivity()).readConfigFile()
        if (storeConfigFile.isEmpty()) {
            CustomDialogs.snackbar(binding.root, "Something has gone wrong...")
            requireActivity().finish()
        } else {
            val configFile = Utilz.convertConfigToModel(storeConfigFile)
            val promo = configFile.promoBanner.find { it.id == 101 }
            binding.apply {
                promoTitle.text = promo?.title
                promoMessage.text = promo?.description
                promoLaunchFab.setOnClickListener {
                    (requireActivity() as IntroSectionActivity).launchApp()
                }
            }
            //Give the User LIFE status
            if (promo?.id == 101)UserProfileHelper.updateUserStatus(promo.promoCode)
        }
    }
}