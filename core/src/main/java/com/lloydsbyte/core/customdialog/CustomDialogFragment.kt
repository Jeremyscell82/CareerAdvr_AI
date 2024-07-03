package com.lloydsbyte.core.customdialog


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lloydsbyte.core.databinding.DialogCustomBinding

class CustomDialogFragment(private val customDialogModel: CustomDialogModel): DialogFragment() {

    lateinit var binding: DialogCustomBinding

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(customDialogModel.cancelable)
        binding.apply {
            dialogTitle.text = customDialogModel.title
            dialogBody.text = customDialogModel.message
            dialogOkButton.text = customDialogModel.okBtn
            if (customDialogModel.cancelBtn == null){
                dialogCancelButton.visibility = View.GONE
            } else {
                dialogCancelButton.text = customDialogModel.cancelBtn
            }
            dialogCancelButton.setOnClickListener {
                customDialogModel.cancelRunnable?.run()
                dialog?.dismiss()
            }
            dialogOkButton.setOnClickListener {
                customDialogModel.okRunnable?.run()
                dialog?.dismiss()
            }
        }
    }
}