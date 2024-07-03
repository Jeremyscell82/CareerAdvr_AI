package com.lloydsbyte.core.custombottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.core.databinding.BottomsheetVersionBinding
import com.lloydsbyte.core.utilz.StoredPref

class VersionBottomsheet: BottomSheetDialogFragment() {
    companion object {
        fun createInstance(title: String, message: String, buildNum: Int): VersionBottomsheet {
            val fragment = VersionBottomsheet()
            fragment.title = title
            fragment.message = message
            fragment.buildNum = buildNum
            return fragment
        }
    }

    var title: String = ""
    var message: String = ""
    var buildNum: Int = 0
    lateinit var binding: BottomsheetVersionBinding

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
        binding.apply {
            versionCloseBttn.setOnClickListener {
                dialog?.dismiss()
            }
            versionTitle.text = title
            versionMessage.text = message
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        StoredPref(requireActivity()).setStoredBuildNum(buildNum)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        StoredPref(requireActivity()).setStoredBuildNum(buildNum)
    }

}