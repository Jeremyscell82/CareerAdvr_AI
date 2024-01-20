package com.lloydsbyte.careeradvr_ai.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.ItemChatBinding
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper.Companion.assistant
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper.Companion.user
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.database.models.ChatModel

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var adapterItems: MutableList<ChatModel> = mutableListOf()

    fun populateChat(animate: Boolean, recyclerView: RecyclerView, chatThread: List<ChatModel>) {
        adapterItems = chatThread.toMutableList()
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

    fun addChat(recyclerView: RecyclerView, question: ChatModel) {
        adapterItems.add(question)
        val anim = AnimationUtils.loadLayoutAnimation(
            recyclerView.context,
            R.anim.recyclerview_animation
        )
        recyclerView.layoutAnimation = anim
        this.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }


    inner class ChatViewHolder(val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatModel: ChatModel) {
            binding.apply {
                itemChatTextview.text = chatModel.content
                cardviewPlacement(this, chatModel)
                itemChatCardview.setOnLongClickListener {
                    Utilz.copyToClipboard(root.context, chatModel.content)
                    CustomDialogs.snackbar(root, "copied")
                    true
                }
            }
        }
    }

    private fun cardviewPlacement(view: ItemChatBinding, chat: ChatModel) {
        //Determines if the item should be on the right or on the left ... user or bot
        if (chat.role == user){
            view.apply {
                itemRootView.gravity = Gravity.END
                itemIconBot.visibility = View.GONE
//                itemIconUser.visibility = View.GONE
            }
        } else if (chat.role == assistant){
            view.apply {
                itemRootView.gravity = Gravity.START
                itemIconBot.visibility = View.VISIBLE
//                itemIconUser.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatViewHolder(ItemChatBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = adapterItems.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(adapterItems[position])
    }
}