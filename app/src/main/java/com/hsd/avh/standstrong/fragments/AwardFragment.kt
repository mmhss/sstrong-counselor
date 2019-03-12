package com.hsd.avh.standstrong.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hsd.avh.standstrong.adapters.AwardAdapter
import com.hsd.avh.standstrong.adapters.PostAdapter
import com.hsd.avh.standstrong.databinding.FragmentAwardsBinding
import com.hsd.avh.standstrong.databinding.FragmentPostBinding
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.AwardViewModel
import com.hsd.avh.standstrong.viewmodels.PostListViewModel

class AwardFragment : Fragment() {

    private lateinit var viewModel: AwardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAwardsBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        val factory = InjectorUtils.provideAwardListViewModelFactory(context)
        viewModel = ViewModelProviders.of(this, factory).get(AwardViewModel::class.java)

        val adapter = AwardAdapter()
        binding.awardList.adapter = adapter
        val swipeRefreshLayout =  binding.swiping
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            viewModel.updateAwardList()
            //Just a hack with no error or success being returned.
            swipeRefreshLayout.postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 3000)

        })
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(adapter: AwardAdapter) {
        viewModel.getAwards().observe(viewLifecycleOwner, Observer { awards->
            if (awards != null) adapter.submitList(awards)
        })
    }


    override fun onResume() {
        super.onResume()
        //StandStrong.firebaseInstance().setCurrentScreen(this!!.activity!!, activity?.javaClass?.simpleName, activity?.javaClass?.simpleName);
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.Awards)
    }

}