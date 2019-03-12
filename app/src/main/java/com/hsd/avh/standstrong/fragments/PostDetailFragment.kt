package com.hsd.avh.standstrong.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.databinding.*
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.*

class PostDetailFragment : Fragment() {

    private lateinit var vm: PostDetailViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //Post Id is
        val postId = PostDetailFragmentArgs.fromBundle(arguments).postId
        val factory = InjectorUtils.providePostDetailViewModelFactory(requireActivity(), postId)
        vm = ViewModelProviders.of(this, factory)
                .get(PostDetailViewModel::class.java)


        val postDetailViewModel = ViewModelProviders.of(this, factory)
                .get(PostDetailViewModel::class.java)


        val binding = DataBindingUtil.inflate<FragmentPostDetailsBinding>(
                inflater, R.layout.fragment_post_details, container, false).apply {
            vm = postDetailViewModel
            setLifecycleOwner(this@PostDetailFragment)
        }


        vm.post.observe(this, Observer { post->
            val postHtml = "file:///android_asset/post_" + post.cardHeader.substring(post.cardHeader.length - 1, post.cardHeader.length) +".html"
            binding.webview.loadUrl(postHtml)
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //StandStrong.firebaseInstance().setCurrentScreen(this!!.activity!!, activity?.javaClass?.simpleName, activity?.javaClass?.simpleName);
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.PostDetails)
    }
}
