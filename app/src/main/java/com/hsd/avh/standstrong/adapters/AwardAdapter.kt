package com.hsd.avh.standstrong.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.databinding.ListItemAwardsBinding
import com.hsd.avh.standstrong.managers.AnalyticsManager

/**
 * Adapter for the [RecyclerView] in [AwardFragment].
 */

class AwardAdapter(val analyticsManager: AnalyticsManager) : ListAdapter<Award, AwardAdapter.ViewHolder>(AwardDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val award = getItem(position)
        holder.apply {
            bind(createOnClickListener(award.awardId), award)
            itemView.tag = award
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemAwardsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(awardId: String): View.OnClickListener {
        return View.OnClickListener {
           // val direction = AwardListFragmentDirections.ActionAwardListFragmentToDetailFragment(awardId)
            //it.findNavController().navigate(direction)

            val args = Bundle()
            args.putString("id", awardId)
            analyticsManager.trackEvent("on award click", args)
            Log.d("SSS",awardId)
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