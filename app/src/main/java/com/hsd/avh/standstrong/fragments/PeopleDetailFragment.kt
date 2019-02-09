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
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.adapters.PostAdapter
import com.hsd.avh.standstrong.databinding.FragmentPeopleDetailsBinding
import com.hsd.avh.standstrong.databinding.FragmentPostBinding
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.PersonDetailViewModel
import com.hsd.avh.standstrong.viewmodels.PostListViewModel
import kotlinx.android.synthetic.main.fragment_post.view.*

class PeopleDetailFragment : Fragment() {

    private lateinit var vm: PersonDetailViewModel



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val personId = PeopleDetailFragmentArgs.fromBundle(arguments).personId
        val binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root
        val factory = InjectorUtils.providePersonDetailViewModelFactory(requireActivity(), personId)

        vm = ViewModelProviders.of(this, factory).get(PersonDetailViewModel::class.java)

        val adapter = PostAdapter()
        binding.postList.adapter = adapter


        subscribeUi(adapter)

        //Dao is LiveData<Int>, Repository os MediatorData and here we observe a change
        vm.getMessageCount().observe(viewLifecycleOwner, Observer { mc: Int ->
            binding.messageCountTextView.text = Integer.toString(mc)  + " " + StandStrong.applicationContext().getString(R.string.messages)
        })
        vm.getPostCount().observe(viewLifecycleOwner, Observer { pc: Int ->
            binding.postCountTextView.text = Integer.toString(pc)  + " " + StandStrong.applicationContext().getString(R.string.posts)
        })
        vm.getAwardCount().observe(viewLifecycleOwner, Observer { ac: Int ->
            binding.awardCountTextView.text = Integer.toString(ac)  + " " + StandStrong.applicationContext().getString(R.string.awards)
        })

        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        //StandStrong.firebaseInstance().setCurrentScreen(this!!.activity!!, activity?.javaClass?.simpleName, activity?.javaClass?.simpleName);
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.ClientDetail)
    }

    private fun subscribeUi(adapter: PostAdapter) {
        vm.getPosts().observe(viewLifecycleOwner, Observer { posts->
            if (posts != null) adapter.submitList(posts)
        })
    }

}
