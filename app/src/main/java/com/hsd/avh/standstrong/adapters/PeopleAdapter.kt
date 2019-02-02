package com.hsd.avh.standstrong.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.databinding.ListItemPeopleBinding
import com.hsd.avh.standstrong.databinding.ListItemPostsBinding
import com.hsd.avh.standstrong.fragments.PeopleFragmentDirections
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.StandStrong


/**
 * Adapter for the [RecyclerView] in [PeopleListFragment].
 */
class PeopleAdapter : ListAdapter<Person, PeopleAdapter.ViewHolder>(PeopleDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = getItem(position)
        holder.apply {
            bind(createOnClickListener(person.personId), person)
            itemView.tag = person
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemPeopleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(personId: String): View.OnClickListener {
        return View.OnClickListener {

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, personId)
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Person Viewed")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "person")
            StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)

            val direction = PeopleFragmentDirections.actionPeopleListToPersonDetail(personId)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(
            private val binding: ListItemPeopleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Person) {
            binding.apply {
                clickListenerPerson = listener
                peopleFragment = item
                executePendingBindings()
            }
        }
    }
}