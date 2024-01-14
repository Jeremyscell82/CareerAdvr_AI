package com.lloydsbyte.careeradvr_ai.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.ItemHistoryBinding
import com.lloydsbyte.careeradvr_ai.utilz.CategoryHelper
import com.lloydsbyte.core.utilz.UtilzDateHelper
import com.lloydsbyte.database.models.ChatHeaderModel

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryAdapterViewHolder>() {

    private var adapterItems: List<ChatHeaderModel> = emptyList()
    private var filteredItems: List<ChatHeaderModel> = emptyList()
    var onItemClicked: ((ChatHeaderModel)->Unit)? = null
    var onLongItemClicked: ((ChatHeaderModel)->Unit)? = null

    fun isAdapterPopulated(): Boolean {
        return adapterItems.isNotEmpty()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun animateAdapter(recyclerView: RecyclerView) {
        val anim = AnimationUtils.loadLayoutAnimation(
            recyclerView.context,
            R.anim.recyclerview_animation
        )
        recyclerView.layoutAnimation = anim
        this.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
    fun initAdapter(chatHeads: List<ChatHeaderModel>, recyclerView: RecyclerView) {
        adapterItems = chatHeads.sortedByDescending { it.conversationID }
        filteredItems = adapterItems
        animateAdapter(recyclerView)
    }

    fun searchItems(query: String, recyclerView: RecyclerView) {
        filteredItems = if (query.isNotEmpty()) {
            adapterItems.filter {
                it.convoName.lowercase().contains(query.lowercase())
            }

        } else {
            //Reset adapter
            adapterItems
        }
        animateAdapter(recyclerView)
    }


    inner class HistoryAdapterViewHolder(val view: ItemHistoryBinding): RecyclerView.ViewHolder(view.root){
        fun bind(chatHeader: ChatHeaderModel) {
            view.apply {

                itemHistoryTitle.text = chatHeader.convoName

                itemHistoryDate.text = UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).convertDateFromMillis(chatHeader.conversationID)

//                itemHistoryQuestionHeader.text = chatHeader.convoName
                itemCategoryImage.setImageDrawable(ResourcesCompat.getDrawable(root.context.resources, CategoryHelper().getIcon(chatHeader.category), null))

                itemHistoryCardview.setOnClickListener {
                    onItemClicked?.invoke(chatHeader)
                }

                itemHistoryCardview.setOnLongClickListener {
                    //Show delete bottomsheet dialog
                    onLongItemClicked?.invoke(chatHeader)
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapterViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return HistoryAdapterViewHolder(ItemHistoryBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun onBindViewHolder(holder: HistoryAdapterViewHolder, position: Int) {
        holder.bind(filteredItems[position])
    }


}