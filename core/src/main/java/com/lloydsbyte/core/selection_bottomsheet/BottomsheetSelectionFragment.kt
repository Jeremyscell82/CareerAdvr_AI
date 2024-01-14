package com.lloydsbyte.core.selection_bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.core.databinding.BottomsheetSelectionBinding

class BottomsheetSelectionFragment(): BottomSheetDialogFragment() {
    companion object {
        fun createInstance(callback: BottomSheetHelper.BottomsheetSelectionInterface, title: String?, items: List<BottomSheetHelper.BottomsheetModel>, singleSelect: Boolean): BottomsheetSelectionFragment {
            val fragment = BottomsheetSelectionFragment()
            fragment.bottomsheetInterface = callback
            fragment.listOfItems = items
            fragment.singleSelect = singleSelect
            fragment.title = title?:""
            return fragment
        }
    }

    lateinit var binding: BottomsheetSelectionBinding
    lateinit var bottomsheetSelectionAdapter: BottomsheetSelectionAdapter
    lateinit var bottomsheetInterface: BottomSheetHelper.BottomsheetSelectionInterface
    var selectedItems: MutableList<BottomSheetHelper.BottomsheetModel> = mutableListOf()
    var listOfItems: List<BottomSheetHelper.BottomsheetModel> = listOf()
    var title: String = ""
    var singleSelect: Boolean = true



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
        binding = BottomsheetSelectionBinding.inflate(inflater, container, false)
        bottomsheetSelectionAdapter = BottomsheetSelectionAdapter()
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
            selectSaveBttn.visibility = if (singleSelect) View.GONE else View.VISIBLE
            selectSaveBttn.setOnClickListener {
                bottomsheetInterface.onSavePressed(selectedItems)
                dialog?.dismiss()
            }
            selectCancelBttn.setOnClickListener {
                dialog?.dismiss()
            }
            selectTitle.text = if (singleSelect) "Select One" else "${selectedItems.size} Selected"
            if (title.isNotEmpty())selectMessageTitle.text = title
            selectRecyclerview.apply {
                bottomsheetSelectionAdapter.initAdapter(listOfItems)
                adapter = bottomsheetSelectionAdapter
                bottomsheetSelectionAdapter.onItemSelected = {
                    itemSelected(it)
                }
            }
        }
    }

    private fun itemSelected(value: BottomSheetHelper.BottomsheetModel){
       if (singleSelect) {
           //Single Select Item.. send it back now
           bottomsheetInterface.onSavePressed(value)
           dialog?.dismiss()
       } else if (value.selected == true){
           //add item to list
           selectedItems.add(value)
           //Update count
       } else {
           selectedItems.remove(value)
       }
        binding.selectTitle.text = "${selectedItems.size} Selected"
    }



}