package com.lloydsbyte.core.search_bottomsheet

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.core.databinding.BottomsheetSearchBinding
import com.lloydsbyte.core.utilz.UtilzViewHelper

class BottomsheetSearchFragment: BottomSheetDialogFragment() {

    lateinit var binding: BottomsheetSearchBinding
    var hint: String = ""
    var listener: BottomsheetSearchInterface? = null

    companion object {
        fun createInstance(hint: String, listener: BottomsheetSearchInterface): BottomsheetSearchFragment {
            val fragment = BottomsheetSearchFragment()
            fragment.listener = listener
            fragment.hint = hint
            return fragment
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            Handler().postDelayed({ UtilzViewHelper.showSoftKeyboard(requireActivity(), binding.searchInputField)}, 300)
            searchInputField.setOnEditorActionListener(TextView.OnEditorActionListener{ view, actionId, _ ->

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listener?.onPhraseEntered(view.text.toString())
                    dialog?.dismiss()
                    return@OnEditorActionListener true
                }
                false
            })

            searchInputLayout.hint = hint

        }
    }
}