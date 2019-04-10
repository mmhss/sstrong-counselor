package com.hsd.avh.standstrong.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.databinding.ListItemPostsBinding
import com.hsd.avh.standstrong.fragments.PostListFragmentDirections
import com.hsd.avh.standstrong.utilities.SSUtils
import com.varunest.sparkbutton.SparkEventListener

class PostsPagedAdapter(val context: Context) : PagedListAdapter<Post, PostsPagedAdapter.PostViewHolder>(PostDiffCallback()) {

    private val TAG = javaClass.canonicalName

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.apply {
                bind(createOnClickListener(post!!), post, createCommentOnClickListener(post))
                itemView.tag = post
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(ListItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(post: Post): View.OnClickListener {
        return View.OnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(post.postId))
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Post Viewed")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "post")
            StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
            //val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
            //val strDate = dateFormat.format(

            when (post.type) {
                StandStrong.POST_CARD_CONTENT ->
                    it.findNavController().navigate(PostListFragmentDirections.actionPostListToPostDetail(post.postId))
                StandStrong.POST_CARD_MESSAGE->
                    it.findNavController().navigate(PostListFragmentDirections.actionPostListToMessages( post.motherId,post.postId))
                StandStrong.POST_CARD_AWARD->
                    it.findNavController().navigate(PostListFragmentDirections.actionPostListToAwards())
                StandStrong.POST_CARD_ACTIVITY ->
                    it.findNavController().navigate(PostListFragmentDirections.actionPostListToDataActivity(post.motherId,post.postDate.time))
                StandStrong.POST_CARD_GPS ->
                    it.findNavController().navigate(PostListFragmentDirections.actionPostListToDataGps(post.motherId,post.postDate.time))
                StandStrong.POST_CARD_PROXIMITY ->
                    it.findNavController().navigate(PostListFragmentDirections.actionPostListToDataProximity(post.motherId,post.postDate.time))
                else -> "na"
            }
            //val direction = PostListFragmentDirections.actionPostListToPostDetail(post.postId)
            //it.findNavController().navigate(direction)
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

    inner class PostViewHolder (val binding: ListItemPostsBinding
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
                    val ret = SSUtils.updateLiked(item.postId, item.liked)
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

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }
    }
}