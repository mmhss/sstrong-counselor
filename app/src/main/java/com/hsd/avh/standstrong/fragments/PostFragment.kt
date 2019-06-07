package com.hsd.avh.standstrong.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hsd.avh.standstrong.adapters.PostAdapter
import com.hsd.avh.standstrong.adapters.PostsPagedAdapter
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.databinding.FragmentPostBinding
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.PostListViewModel


class PostListFragment : BaseFragment() {

    private lateinit var viewModel: PostListViewModel
    private var hasPosts:Boolean = false
    private lateinit var binding: FragmentPostBinding
    private val TAG = javaClass.canonicalName

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        val factory = InjectorUtils.providePostListViewModelFactory(context)

        viewModel = activity?.run {
            ViewModelProviders.of(this, factory).get(PostListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        val adapter = PostsPagedAdapter(activity!!, analyticsManager)
        binding.postList.adapter = adapter

        val fab =  binding.fab2
        fab.setOnClickListener{
            val dialogFrag = FilterPostFabFragment.newInstance()
            dialogFrag.setParentFab(fab)
            dialogFrag.show(fragmentManager, dialogFrag.tag)

            analyticsManager.trackEvent("Open filter for post click")
        }

        val swipeRefreshLayout =  binding.swiping
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.updatePosts()
            //Just a hack with no error or success being returned.
            swipeRefreshLayout.postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 3000)

        }
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(adapter: PostsPagedAdapter) {

        viewModel.getPostsPaged().observe(this, Observer {
            if (it != null) {
                adapter.submitList(it)
                binding.postList.scrollToPosition(0)
                binding.noPosts.visibility =  View.GONE;
            }
            if (it.isNullOrEmpty()){
                binding.noPosts.visibility =  View.VISIBLE;
            }
        })
    }

}