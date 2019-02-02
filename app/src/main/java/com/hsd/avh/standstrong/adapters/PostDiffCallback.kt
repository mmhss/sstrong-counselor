package com.hsd.avh.standstrong.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hsd.avh.standstrong.data.posts.Post

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.postId== newItem.postId
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}