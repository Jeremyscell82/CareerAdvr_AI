package com.lloydsbyte.careeradvr_ai.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.careeradvr_ai.databinding.BottomsheetAboutBinding
import com.lloydsbyte.core.utilz.Utilz

class HistoryChatBottomsheet: BottomSheetDialogFragment() {

    lateinit var binding: BottomsheetAboutBinding
    private val profileUrl: String = "https://play.google.com/store/apps/dev?id=8668340116169178413"

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
            Utilz.openInBrowser(requireActivity(), profileUrl)
        }
    }
}