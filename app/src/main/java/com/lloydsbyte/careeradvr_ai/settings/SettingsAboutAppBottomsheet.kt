package com.lloydsbyte.careeradvr_ai.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.careeradvr_ai.databinding.BottomsheetAboutappBinding


class SettingsAboutAppBottomsheet: BottomSheetDialogFragment() {

    lateinit var binding: BottomsheetAboutappBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetAboutappBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomsheetFabClose.setOnClickListener {
            dialog?.dismiss()
        }
    }
}