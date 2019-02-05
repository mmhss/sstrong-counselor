package com.hsd.avh.standstrong.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.databinding.ListItemPostsBinding
import com.hsd.avh.standstrong.fragments.PostListFragmentDirections
import com.hsd.avh.standstrong.utilities.SSUtils
import com.varunest.sparkbutton.SparkEventListener

/**
 * Adapter for the [RecyclerView] in [PostListFragment].
 */
class PostAdapter : ListAdapter<Post, PostAdapter.ViewHolder>(PostDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.apply {
            bind(createOnClickListener(post.postId), post,createCommentOnClickListener(post))
            itemView.tag = post
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemPostsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(postId: Int): View.OnClickListener {
        return View.OnClickListener {

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(postId))
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Post Viewed")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "post")
            StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)

            val direction = PostListFragmentDirections.actionPostListToPostDetail(postId)
            it.findNavController().navigate(direction)
        }
    }

    private fun createCommentOnClickListener(post: Post): View.OnClickListener {
        return View.OnClickListener {

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(post.postId))
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Comments Viewed")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "post")
            StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
            val direction = PostListFragmentDirections.actionPostListToMessages( post.motherId,post.postId)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(
        private val binding: ListItemPostsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Post, commentListener : View.OnClickListener) {

            binding.sparkButton.setEventListener(object : SparkEventListener {
                override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
                }

                override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {
                }

                override fun onEvent(button: ImageView?, buttonState: Boolean) {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(item.postId))
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Liked")
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "post")

                    //UPDATE USING DAO
                    val ret = SSUtils.updateLiked(item.postId,item.liked)
                    StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
                }

            })

            binding.apply {
                clickListener = listener
                commentClickListener = commentListener
                post = item
                executePendingBindings()
            }
        }


    }
}