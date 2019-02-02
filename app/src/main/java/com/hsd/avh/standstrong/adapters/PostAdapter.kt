package com.hsd.avh.standstrong.adapters

import android.os.Bundle
import android.util.StatsLog.logEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.databinding.ListItemPostsBinding
import com.hsd.avh.standstrong.fragments.PeopleFragmentDirections
import com.hsd.avh.standstrong.fragments.PostListFragmentDirections

/**
 * Adapter for the [RecyclerView] in [PostListFragment].
 */
class PostAdapter : ListAdapter<Post, PostAdapter.ViewHolder>(PostDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.apply {
            bind(createOnClickListener(post.postId), post)
            itemView.tag = post
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemPostsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(postId: String): View.OnClickListener {
        return View.OnClickListener {

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, postId)
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Post Viewed")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "post")
            StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)

            val direction = PostListFragmentDirections.actionPostListToPostDetail(postId)
            it.findNavController().navigate(direction)
        }
    }
    class ViewHolder(
        private val binding: ListItemPostsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Post) {
            binding.apply {
                clickListener = listener
                post = item
                executePendingBindings()
            }
        }
    }
}