package com.lloydsbyte.core.custombottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.core.databinding.BottomsheetErrorBinding

/**
 * To be used during testing to allow the user to see the error and report it if they notice it in other areas too.
 * The error will be reported prior to launching this dialog as it does not have access to the main app and it's analytics classes
 */
class ErrorBottomsheet : BottomSheetDialogFragment() {

    companion object {
        fun createInstance(errorTitle: String, errorMessage: String?): ErrorBottomsheet {
            val fragment = ErrorBottomsheet()
            fragment.title = errorTitle
            fragment.message = errorMessage?:"NA"
            return fragment
        }
    }

    var title: String = ""
    var message: String = ""
    lateinit var binding: BottomsheetErrorBinding

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
        binding = BottomsheetErrorBinding.inflate(inflater, container, false)
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
            errorCloseBttn.setOnClickListener {
                dialog?.dismiss()
            }
            errorTitle.text = title
            errorMessage.text = message
        }
    }
}