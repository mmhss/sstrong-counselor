package com.hsd.avh.standstrong.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hsd.avh.standstrong.adapters.AwardAdapter
import com.hsd.avh.standstrong.databinding.FragmentAwardsBinding
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.AwardViewModel

class AwardFragment : BaseFragment() {

    private lateinit var viewModel: AwardViewModel
    private lateinit var binding:FragmentAwardsBinding
    private val TAG = javaClass.canonicalName

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAwardsBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        val factory = InjectorUtils.provideAwardListViewModelFactory(context)
        viewModel = ViewModelProviders.of(this, factory).get(AwardViewModel::class.java)

        val adapter = AwardAdapter(analyticsManager)
        binding.awardList.adapter = adapter
        val swipeRefreshLayout =  binding.swiping
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            viewModel.updateAwardList()
            //Just a hack with no error or success being returned.
            swipeRefreshLayout.postDelayed({
                swipeRefreshLayout.isRefreshing = false
                analyticsManager.trackEvent("Refresh of awards launched by swipe")
            }, 3000)

        })
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(adapter: AwardAdapter) {

        viewModel.subscribeOnAwards().observe(this, Observer {

            if (it.isNotEmpty()) {
                binding.noAwards.visibility = View.GONE
                adapter.submitList(it)
            } else {
                binding.noAwards.visibility = View.VISIBLE
            }
        })
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateAwardList()
    }

}