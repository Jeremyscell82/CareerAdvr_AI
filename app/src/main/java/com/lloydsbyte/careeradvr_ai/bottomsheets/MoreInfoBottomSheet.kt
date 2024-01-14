package com.lloydsbyte.careeradvr_ai.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.BottomsheetMoreinfoBinding

class MoreInfoBottomSheet: BottomSheetDialogFragment() {
    companion object {
        fun createInstance(title: String, message: String): MoreInfoBottomSheet {
            val bottomsheet = MoreInfoBottomSheet()
            bottomsheet.title = title
            bottomsheet.message = message
            return bottomsheet
        }
    }

    lateinit var binding: BottomsheetMoreinfoBinding
    var title: String = ""
    var message: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetMoreinfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(true)
        binding.apply {
            dialogTitle.text = title
            dialogMessage.text = message
            Glide.with(root)
                .load(ResourcesCompat.getDrawable(resources, R.drawable.ic_chatbot, null))
                .into(dialogImage)
            dialogClose.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }
}