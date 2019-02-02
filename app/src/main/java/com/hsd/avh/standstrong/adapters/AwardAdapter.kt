package com.hsd.avh.standstrong.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.databinding.ListItemAwardsBinding

/**
 * Adapter for the [RecyclerView] in [AwardFragment].
 */
class AwardAdapter : ListAdapter<Award, AwardAdapter.ViewHolder>(AwardDiffCallback()) {

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