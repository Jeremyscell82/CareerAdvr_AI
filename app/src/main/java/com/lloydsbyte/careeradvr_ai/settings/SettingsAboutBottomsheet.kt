package com.lloydsbyte.careeradvr_ai.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.careeradvr_ai.databinding.BottomsheetAboutBinding
import com.lloydsbyte.core.utilz.UtilzSendItHelper
import com.lloydsbyte.core.utilz.Utilz


class SettingsAboutBottomsheet: BottomSheetDialogFragment() {

    lateinit var binding: BottomsheetAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetAboutBinding.inflate(inflater, container, false)
        //Set fullscreen
        dialog?.setOnShowListener { dialog ->
            val layout: FrameLayout? =
                (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)
            layout?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomsheetCloseBtn.setOnClickListener {
            dialog?.dismiss()
        }
        binding.aboutAppButton.setOnClickListener {
            Utilz.openInBrowser(requireActivity(), UtilzSendItHelper.profileUrl)
        }
    }
}