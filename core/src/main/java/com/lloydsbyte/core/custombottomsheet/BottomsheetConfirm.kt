package com.lloydsbyte.core.custombottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.core.databinding.BottomsheetConfirmDeletionBinding
import com.lloydsbyte.database.DatabaseInterface

class BottomsheetConfirm: BottomSheetDialogFragment(), DatabaseInterface {

    companion object {
        fun createInstance(callback: ConfirmInterface, title: String, message: String, actionBtnTitle: String): BottomsheetConfirm {
            val bottomsheet = BottomsheetConfirm()
            bottomsheet.title = title
            bottomsheet.message = message
            bottomsheet.actionButton = actionBtnTitle
            bottomsheet.callback = callback
            return bottomsheet
        }
    }


    lateinit var binding: BottomsheetConfirmDeletionBinding
    lateinit var callback: ConfirmInterface
    var title: String = ""
    var message: String = ""
    var actionButton: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetConfirmDeletionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(true)
        binding.apply {
            dialogTitle.text = title
            dialogMessage.text = message

            dialogClose.setOnClickListener {
                dialog?.dismiss()
            }
            dialogActionButton.text = actionButton
            dialogActionButton.setOnClickListener{
                callback.onActionPressed()
                dialog?.dismiss()
            }
        }
    }

    override fun onComplete() {
        dialog?.dismiss()
    }

}