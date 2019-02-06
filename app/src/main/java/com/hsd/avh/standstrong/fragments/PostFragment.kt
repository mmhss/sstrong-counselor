package com.hsd.avh.standstrong.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hsd.avh.standstrong.adapters.PostAdapter
import com.hsd.avh.standstrong.databinding.FragmentPostBinding
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.PostListViewModel


class PostListFragment : Fragment() {

    private lateinit var viewModel: PostListViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPostBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root
        val factory = InjectorUtils.providePostListViewModelFactory(context)
        viewModel = ViewModelProviders.of(this, factory).get(PostListViewModel::class.java)

        val adapter = PostAdapter()
        binding.postList.adapter = adapter
        val swipeRefreshLayout =  binding.swiping
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.updatePeople()
            //Just a hack with no error or success being returned.
            swipeRefreshLayout.postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 3000)

        }

        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        //StandStrong.firebaseInstance().setCurrentScreen(this!!.activity!!, activity?.javaClass?.simpleName, activity?.javaClass?.simpleName);
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.Post)
    }

    private fun subscribeUi(adapter: PostAdapter) {
        viewModel.getPosts().observe(viewLifecycleOwner, Observer { posts->
            if (posts != null) adapter.submitList(posts)
        })
    }

}