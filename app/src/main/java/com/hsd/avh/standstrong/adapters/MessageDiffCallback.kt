package com.hsd.avh.standstrong.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.posts.Post

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}