package com.lloydsbyte.core.custombottomsheet

import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.core.R
import com.lloydsbyte.core.databinding.BottomsheetEditfieldBinding
import com.lloydsbyte.core.search_bottomsheet.BottomsheetSearchInterface
import com.lloydsbyte.core.utilz.UtilzViewHelper

class BottomsheetEditFieldFragment: BottomSheetDialogFragment() {

    lateinit var binding: BottomsheetEditfieldBinding
    var hint: String = ""
    var phrase: String = ""
    lateinit var listener: BottomsheetSearchInterface
    var showNumberKeyboard: Boolean = false

    companion object {
        fun createInstance(hint: String, phrase: String, number: Boolean, listener: BottomsheetSearchInterface): BottomsheetEditFieldFragment {
            val fragment = BottomsheetEditFieldFragment()
            fragment.hint = hint
            fragment.phrase = phrase
            fragment.showNumberKeyboard = number
            fragment.listener = listener
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetEditfieldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            if (showNumberKeyboard)bottomsheetInputField.inputType = InputType.TYPE_CLASS_NUMBER

            Handler().postDelayed({ UtilzViewHelper.showSoftKeyboard(requireActivity(), bottomsheetInputField)}, 300)

            selectCancelBttn.setOnClickListener {
                dialog?.dismiss()
            }
            selectSaveBttn.text = if (phrase.isEmpty())resources.getString(R.string.save) else resources.getString(R.string.update)
            selectSaveBttn.setOnClickListener {
                listener.onPhraseEntered(bottomsheetInputField.text.toString())
                dialog?.dismiss()
            }
            bottomsheetInputField.setOnEditorActionListener(TextView.OnEditorActionListener{ view, actionId, _ ->

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    listener.onPhraseEntered(bottomsheetInputField.text.toString())
                    dialog?.dismiss()
                    return@OnEditorActionListener true
                }
                false
            })
            bottomsheetInputLayout.hint = hint
            bottomsheetInputField.setText(phrase)

        }
    }

}