package com.lloydsbyte.careeradvr_ai.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.ItemProSpecificBinding
import com.lloydsbyte.network.ConfigModel

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var adapterItems: List<ConfigModel.Prompt> = emptyList()
    var onItemClicked: ((ConfigModel.Prompt) -> Unit)? = null
    var onItemLongClicked: ((ConfigModel.Prompt) -> Unit)? = null

    fun initAdapter(animate: Boolean, recyclerView: RecyclerView, items: List<ConfigModel.Prompt>) {
        adapterItems = items
        if (animate){
            val anim = AnimationUtils.loadLayoutAnimation(
                recyclerView.context,
                R.anim.recyclerview_animation
            )
            recyclerView.layoutAnimation = anim
            this.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        } else {
            this.notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(ItemProSpecificBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = adapterItems.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(adapterItems[position])
    }

    inner class HomeViewHolder(val binding: ItemProSpecificBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(prompt: ConfigModel.Prompt) {
            binding.apply {
                itemTitle.text = prompt.title
                itemCardview.setOnClickListener {
                    onItemClicked?.invoke(prompt)
                }
                itemCardview.setOnLongClickListener {
                    //Show more info dialog
                    onItemLongClicked?.invoke(prompt)
                    false
                }
            }
        }
    }
}