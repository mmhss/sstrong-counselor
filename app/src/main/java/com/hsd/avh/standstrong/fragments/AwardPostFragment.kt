package com.hsd.avh.standstrong.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.databinding.ListItemAwardsBinding
import com.hsd.avh.standstrong.utilities.Const
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.AwardViewModel
import java.text.SimpleDateFormat

class AwardPostFragment : Fragment() {

    lateinit var binding: ListItemAwardsBinding
    lateinit var viewModel: AwardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.list_item_awards, container, false)

        binding.clickListenerAward = null

        val context = context ?: return binding.root

        val factory = InjectorUtils.provideAwardListViewModelFactory(context)
        viewModel = ViewModelProviders.of(this, factory).get(AwardViewModel::class.java)

        initPost(AwardPostFragmentArgs.fromBundle(arguments).awardId)

        return binding.root
    }

    private fun initPost(awardId: String) {

        viewModel.subscribeOnAward(awardId).observe(viewLifecycleOwner, Observer {

            binding.awardFragment = it

            binding.awardDate.text = SimpleDateFormat(Const.DEFAULT_DATE_FORMAT).format(it.awardDate)
        })
    }


}
