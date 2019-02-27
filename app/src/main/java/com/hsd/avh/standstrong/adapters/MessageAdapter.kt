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
import com.hsd.avh.standstrong.databinding.ListItemMsgMeBinding
import com.hsd.avh.standstrong.databinding.ListItemMsgYouBinding

/**
 * Adapter for the [RecyclerView] in [MessageFragment].
 */
class MessageAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {


    override  fun onBindViewHolder(holder:RecyclerView.ViewHolder , position:Int) {
        val msg = getItem(position)
        if (msg.direction == StandStrong.MESSAGE_DIRECTION_IN) {
            val viewHolderYou:ViewHolderYou = holder as ViewHolderYou
            viewHolderYou.apply {
                bind(msg) //, createHeartOnClickListener(post)
                itemView.tag = msg
            }
        } else {
            val viewHolderMe:ViewHolderMe = holder as ViewHolderMe
            viewHolderMe.apply {
                bind(msg) //, createHeartOnClickListener(post)
                itemView.tag = msg
            }
        }

    }

    @Override
    override  fun onCreateViewHolder( parent:ViewGroup, viewType:Int) : RecyclerView.ViewHolder {

        return if (viewType == 0)
            ViewHolderYou(com.hsd.avh.standstrong.databinding.ListItemMsgYouBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
        else
            ViewHolderMe(com.hsd.avh.standstrong.databinding.ListItemMsgMeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))

    }


    override fun getItemViewType(position: Int): Int {
        val msg = getItem(position)
        return if (msg.direction == StandStrong.MESSAGE_DIRECTION_IN) {
            0
        } else
            1
    }

    private fun createMessageButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "New Message")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "msg")
            StandStrong.firebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
        }
    }


    class ViewHolderMe(
            private val binding: ListItemMsgMeBinding
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

    class ViewHolderYou(
            private val binding: ListItemMsgYouBinding
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