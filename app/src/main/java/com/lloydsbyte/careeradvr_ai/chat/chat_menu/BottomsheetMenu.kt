package com.lloydsbyte.careeradvr_ai.chat.chat_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.careeradvr_ai.databinding.BottomsheetMenuBinding

class BottomsheetMenu(val callback: MenuInterface): BottomSheetDialogFragment()  {

    lateinit var binding: BottomsheetMenuBinding
    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetMenuBinding.inflate(inflater, container, false)
        dialog?.setOnShowListener { dialog ->
            val layout: FrameLayout? = (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)
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
        binding.apply {
            chatMenuSave.setOnClickListener {
                callback.onItemClicked("save")
                dialog?.dismiss()
            }
            chatMenuShare.setOnClickListener {
                callback.onItemClicked("share")
                dialog?.dismiss()
            }
        }
    }

}