package com.lloydsbyte.careeradvr_ai.bottomsheets

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.careeradvr_ai.analytics.MixPanelController
import com.lloydsbyte.careeradvr_ai.databinding.BottomsheetChatRatingBinding

class RateDialogFragment(

): BottomSheetDialogFragment() {
    companion object {
        fun createInstance(title: String, message: String, drawable: Drawable?, eventValueName: String): RateDialogFragment {
            val fragment = RateDialogFragment()
            fragment.title = title
            fragment.message = message
            fragment.drawableToUse = drawable
            fragment.eventValueName = eventValueName
            return fragment
        }
    }

    lateinit var binding: BottomsheetChatRatingBinding
    lateinit var title: String
    lateinit var message: String
    var drawableToUse: Drawable? = null
    lateinit var eventValueName: String //Either AMA or PRO




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetChatRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(true)
        binding.apply {
            dialogTitle.text = title
            if (drawableToUse != null)dialogImage.setImageDrawable(drawableToUse)
            dialogClose.setOnClickListener {
                dialog?.dismiss()
            }
            if (message.isNotEmpty()){
                dialogMessage.visibility = View.VISIBLE
                dialogMessage.text = message
            }

            dialogThumbsUp.setOnClickListener {
                MixPanelController().reportChatRating(requireActivity(), eventValueName, "Thumbs Up")
                dialog?.dismiss()
            }
            dialogThumbsDown.setOnClickListener {
                MixPanelController().reportChatRating(requireActivity(), eventValueName, "Thumbs Down")
                dialog?.dismiss()
            }
        }
    }
}