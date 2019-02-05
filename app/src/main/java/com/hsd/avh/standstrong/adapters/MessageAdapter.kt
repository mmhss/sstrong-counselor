package com.hsd.avh.standstrong.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.databinding.ListItemMessagesBinding

/**
 * Adapter for the [RecyclerView] in [MessageFragment].
 */
class MessageAdapter : ListAdapter<Message, MessageAdapter.ViewHolder>(MessageDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = getItem(position)
        holder.apply {
            bind(msg) //, createHeartOnClickListener(post)
            itemView.tag = msg
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(com.hsd.avh.standstrong.databinding.ListItemMessagesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createMessageButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "New Message")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "msg")
            StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
        }
    }


    class ViewHolder(
        private val binding: ListItemMessagesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Message) {  //, item: Post, heartListener : View.OnClickListener

            binding.apply {
                //sendClickListener = listener
                //keepClickListener = heartListener
                msg = item
                executePendingBindings()
            }
        }


    }
}