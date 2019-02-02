package com.hsd.avh.standstrong.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.posts.Post

class AwardDiffCallback : DiffUtil.ItemCallback<Award>() {

    override fun areItemsTheSame(oldItem: Award, newItem: Award): Boolean {
        return oldItem.awardId == newItem.awardId
    }

    override fun areContentsTheSame(oldItem: Award, newItem: Award): Boolean {
        return oldItem == newItem
    }
}