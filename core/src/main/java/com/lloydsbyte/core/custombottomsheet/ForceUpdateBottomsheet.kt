package com.lloydsbyte.core.custombottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.core.databinding.BottomsheetVersionBinding
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.core.utilz.UtilzSendItHelper

class ForceUpdateBottomsheet:  BottomSheetDialogFragment() {

    companion object {
        fun createInstance(title: String, message: String): ForceUpdateBottomsheet {
            val fragment = ForceUpdateBottomsheet()
            fragment.title = title
            fragment.message = message
            return fragment
        }
    }

    lateinit var binding: BottomsheetVersionBinding
    var title: String = ""
    var message: String = ""

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
        binding = BottomsheetVersionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.apply {
            isCancelable = false

        }
        binding.apply {
            versionCloseBttn.setOnClickListener {
//                dialog?.dismiss()
            }
            versionCloseBttn.visibility = View.GONE
            versionTitle.text = title
            versionMessage.text = message
            versionUpdateFab.visibility = View.VISIBLE
            versionUpdateFab.setOnClickListener {
                //Send to play store to update app
                Utilz.openInBrowser(requireActivity(), UtilzSendItHelper.appUrl)
            }
        }
    }

}