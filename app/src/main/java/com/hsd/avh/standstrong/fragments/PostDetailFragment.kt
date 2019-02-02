package com.hsd.avh.standstrong.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.adapters.PeopleAdapter
import com.hsd.avh.standstrong.adapters.PostAdapter
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
        val postId = PostDetailFragmentArgs.fromBundle(arguments).postId

        //val binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)
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


        /*val binding = DataBindingUtil.inflate<FragmentPeopleDetailsBinding>(
                inflater, R.layout.fragment_people_details, container, false).apply {
            vm = personDetailViewModel
            setLifecycleOwner(this@PeopleDetailFragment)

        }
*/
        vm.post.observe(this, Observer { post->
            Snackbar.make(binding.root, "Post", Snackbar.LENGTH_LONG).show()
        })
    /*    personDetailViewModel.person.observe(this, Observer { person->
            shareText = if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_plant, plant.name)
            }
        })
*/

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //StandStrong.firebaseInstance().setCurrentScreen(this!!.activity!!, activity?.javaClass?.simpleName, activity?.javaClass?.simpleName);
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.PostDetails)
    }
}
