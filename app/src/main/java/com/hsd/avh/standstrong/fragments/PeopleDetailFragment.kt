package com.hsd.avh.standstrong.fragments



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hsd.avh.standstrong.adapters.PostsPagedAdapter
import com.hsd.avh.standstrong.databinding.FragmentPeopleDetailsBinding
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.PersonDetailViewModel
import kotlinx.android.synthetic.main.fragment_people_details.*

class PeopleDetailFragment : BaseFragment() {

    private val TAG = javaClass.canonicalName

    private lateinit var vm: PersonDetailViewModel
    private lateinit var binding:FragmentPeopleDetailsBinding
    private lateinit var postsPagedAdapter: PostsPagedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postsPagedAdapter = PostsPagedAdapter(activity!!, analyticsManager)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val personId = PeopleDetailFragmentArgs.fromBundle(arguments).personId
        val motherId = PeopleDetailFragmentArgs.fromBundle(arguments).motherId
        binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root
        val factory = InjectorUtils.providePersonDetailViewModelFactory(requireActivity(), personId)

       // vm = ViewModelProviders.of(this, factory).get(PersonDetailViewModel::class.java)
        vm  = activity?.run {
            ViewModelProviders.of(activity!!, factory).get(PersonDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        vm.updatePostList(personId)

        binding.postList.layoutManager = LinearLayoutManager(activity!!)
        binding.postList.adapter = postsPagedAdapter
        subscribeUi()

        //Dao is LiveData<Int>, Repository os MediatorData and here we observe a change
        vm.getMessageCount().observe(viewLifecycleOwner, Observer { mc: Int ->
            //binding.messageCountTextView.text = Integer.toString(mc)  + " " + StandStrong.applicationContext().getString(R.string.messages)
        })
        vm.getPostCount().observe(viewLifecycleOwner, Observer { pc: Int ->
            //binding.postCountTextView.text = Integer.toString(pc)  + " " + StandStrong.applicationContext().getString(R.string.posts)
        })
        /*vm.getAwardCount().observe(viewLifecycleOwner, Observer { ac: Int ->
            binding.awardCountTextView.text = Integer.toString(ac)  + " " + StandStrong.applicationContext().getString(R.string.awards)
 a        })
*/

        val fab =  binding.fab3
        fab.setOnClickListener{
            val dialogFrag = FilterPersonFabFragment.newInstance(personId, motherId)
            dialogFrag.setParentFab(fab)
            dialogFrag.show(fragmentManager, dialogFrag.tag)

            analyticsManager.trackEvent("Open filter for people click")
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (postsPagedAdapter != null)
            appBar.setExpanded(false, false)
    }

    private fun subscribeUi() {

        vm.getPersonPostsPaged().observe(this, Observer { posts ->

            if (posts != null) {

                postsPagedAdapter.submitList(posts)
                binding.noPosts.visibility =  View.GONE
            }
            if (posts.isNullOrEmpty()){
                binding.noPosts.visibility =  View.VISIBLE
            }
        })

        vm.subscribeOnScrollToTop().observe(this, Observer {

            Log.d(TAG, "need scroll ? $it")

            if (it!!) {
                binding.postList.layoutManager?.scrollToPosition(0)
            }
        })
    }

}
