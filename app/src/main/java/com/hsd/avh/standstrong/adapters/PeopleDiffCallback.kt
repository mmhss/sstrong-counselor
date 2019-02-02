package com.hsd.avh.standstrong.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.posts.Post

class PeopleDiffCallback : DiffUtil.ItemCallback<Person>() {

    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.personId== newItem.personId
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}