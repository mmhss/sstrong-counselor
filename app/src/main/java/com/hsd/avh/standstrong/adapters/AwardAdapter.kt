package com.hsd.avh.standstrong.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.databinding.ListItemAwardsBinding
import com.hsd.avh.standstrong.fragments.AwardFragmentDirections
import com.hsd.avh.standstrong.fragments.AwardPostFragment
import com.hsd.avh.standstrong.fragments.AwardPostFragmentArgs
import com.hsd.avh.standstrong.fragments.PostListFragmentDirections
import com.hsd.avh.standstrong.managers.AnalyticsManager

/**
 * Adapter for the [RecyclerView] in [AwardFragment].
 */

class AwardAdapter(val analyticsManager: AnalyticsManager) : ListAdapter<Award, AwardAdapter.ViewHolder>(AwardDiffCallback()) {

    private val TAG = javaClass.canonicalName

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val award = getItem(position)
        holder.apply {
            bind(createOnClickListener(award), award)
            itemView.tag = award
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemAwardsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(award: Award): View.OnClickListener {
        return View.OnClickListener {
           // val direction = AwardListFragmentDirections.ActionAwardListFragmentToDetailFragment(awardId)
            //it.findNavController().navigate(direction)

            Log.d(TAG, "award = $award")

            val args = Bundle()
            args.putString("id", award.awardId)
            analyticsManager.trackEvent("on award click", args)
            Log.d("SSS",award.awardId)

            it.findNavController().navigate(AwardFragmentDirections.actionTabAwardsToAwardPostFragment(award.awardId))
        }
    }

    class ViewHolder(
        private val binding: ListItemAwardsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Award) {
            binding.apply {
                clickListenerAward = listener
                awardFragment = item
                executePendingBindings()
            }
        }
    }
}