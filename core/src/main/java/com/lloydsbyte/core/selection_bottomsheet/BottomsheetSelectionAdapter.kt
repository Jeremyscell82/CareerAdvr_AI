package com.lloydsbyte.core.selection_bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lloydsbyte.core.databinding.ItemBottomsheetSelectionBinding

/**
 * This adapter is for the single selection bottomsheet, when one is pressed it will save and close
 * The way this system works is by using the stored preferences to store the values
 */
class BottomsheetSelectionAdapter: RecyclerView.Adapter<BottomsheetSelectionAdapter.SingleSelectionViewHolder>() {

    //Used for topics and countries (maybe)
    fun initAdapter(items: List<BottomSheetHelper.BottomsheetModel>) {
        adapterItems = items
    }

    var adapterItems: List<BottomSheetHelper.BottomsheetModel> = emptyList()
    var onItemSelected: ((BottomSheetHelper.BottomsheetModel) -> Unit)? = null


    inner class SingleSelectionViewHolder(val view: ItemBottomsheetSelectionBinding) : RecyclerView.ViewHolder(view.root){
        fun bind(item: BottomSheetHelper.BottomsheetModel) {
            view.apply {
                itemTitle.text = item.name
                itemRootView.setOnClickListener {
                    item.selected = if (item.selected == null)true else !item.selected!!
                    setSelectedState(item.selected!!)
                    onItemSelected?.invoke(item)
                }
            }
        }

        private fun setSelectedState(selected: Boolean) {
            view.itemSelectedFab.visibility = if (selected) View.VISIBLE else View.GONE

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleSelectionViewHolder {
        return SingleSelectionViewHolder(ItemBottomsheetSelectionBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SingleSelectionViewHolder, position: Int) {
        holder.bind(adapterItems[position])
    }

    override fun getItemCount(): Int = if (adapterItems.isNotEmpty())adapterItems.size else 0

}