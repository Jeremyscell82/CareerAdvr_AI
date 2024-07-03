package com.lloydsbyte.chap_e.chat.menu

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lloydsbyte.careeradvr_ai.databinding.DialogFragmentMenuBinding

class MenuDialogFragment(private val menuInterface: MenuInterface): DialogFragment() {

    lateinit var binding: DialogFragmentMenuBinding

    override fun onStart() {
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(true)
        binding.apply {
            menuSave.setOnClickListener {
                menuInterface.onSave()
                dialog?.dismiss()
            }
            menuShare.setOnClickListener {
                menuInterface.onShare()
                dialog?.dismiss()
            }
            menuReport.setOnClickListener {
                menuInterface.onReport()
                dialog?.dismiss()
            }
            menuBackground.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }
}