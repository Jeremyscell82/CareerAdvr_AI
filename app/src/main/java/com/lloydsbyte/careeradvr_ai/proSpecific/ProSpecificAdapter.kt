package com.lloydsbyte.careeradvr_ai.proSpecific

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.ItemProSpecificBinding
import com.lloydsbyte.careeradvr_ai.utilz.UserProfileHelper
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.network.ConfigModel
import timber.log.Timber

class ProSpecificAdapter: RecyclerView.Adapter<ProSpecificAdapter.ProSpecificViewHolder>() {

    var adapterItems: List<ConfigModel.Prompt> = emptyList()
    var onItemClicked: ((ConfigModel.Prompt) -> Unit)? = null
    var onItemLongClicked: ((ConfigModel.Prompt) -> Unit)? = null

    fun initAdapter(animate: Boolean, recyclerView: RecyclerView, professions: List<ConfigModel.Prompt>) {
        adapterItems = professions
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

    inner class ProSpecificViewHolder(val view: ItemProSpecificBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(prompt: ConfigModel.Prompt) {
            view.apply {
                val locked: Boolean = checkStatus(root.context, prompt.payWall)
                itemTitle.text = prompt.title
                itemCardview.setOnClickListener {
                    if (!locked)onItemClicked?.invoke(prompt)
                }
                itemCardview.setOnLongClickListener {
                    //Show more info dialog
                    onItemLongClicked?.invoke(prompt)
                    false
                }

                itemLockLayout.visibility = if (locked) View.VISIBLE else View.GONE
            }
        }
    }

    private fun checkStatus(context: Context, lockStatus: Boolean): Boolean {
        val test = StoredPref(context).getMembershipStatus()
        Timber.d("JL_ test locking system: $test")
        return when (test){
            UserProfileHelper.statusFree -> lockStatus
            UserProfileHelper.statusPaid -> false
            UserProfileHelper.statusSub -> false
            UserProfileHelper.statusLife -> false
            else -> {lockStatus}
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProSpecificViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProSpecificViewHolder(ItemProSpecificBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = adapterItems.size

    override fun onBindViewHolder(holder: ProSpecificViewHolder, position: Int) {
        holder.bind(adapterItems[position])
    }
}