package com.lloydsbyte.core.selection_bottomsheet

class BottomSheetHelper {

    //Easily Create a list of models
    //For demo val titles = resources.getStringArray(R.array.demo_selection_list)
    fun createBottomSheetList(titles: List<String>): List<BottomsheetModel> {
        var items: MutableList<BottomsheetModel> = mutableListOf()
        //A for loop with an index to use with the
        for ((index, title) in titles.withIndex()){
            items.add(BottomsheetModel(index, title, null))
        }
        return items.toList()
    }

    //Model, selected used for the adapter only, could be ignored
    data class BottomsheetModel(
        val id: Int,
        val name: String,
        var selected: Boolean?
    )

    //Interface
    interface BottomsheetSelectionInterface {
        fun onSavePressed(itemsSelected: List<BottomsheetModel>)
        fun onSavePressed(itemSelected: BottomsheetModel)
        fun onCancelPressed()

    }
}