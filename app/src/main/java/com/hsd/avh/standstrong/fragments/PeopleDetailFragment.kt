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
import com.hsd.avh.standstrong.databinding.FragmentPeopleDetailsBinding
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.PersonDetailViewModel

class PeopleDetailFragment : Fragment() {

    private lateinit var vm: PersonDetailViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val personId = PeopleDetailFragmentArgs.fromBundle(arguments).personId

        //val binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)
        val factory = InjectorUtils.providePersonDetailViewModelFactory(requireActivity(), personId)

        vm = ViewModelProviders.of(this, factory)
                .get(PersonDetailViewModel::class.java)

        val personDetailViewModel = ViewModelProviders.of(this, factory)
                .get(PersonDetailViewModel::class.java)


        val binding = DataBindingUtil.inflate<FragmentPeopleDetailsBinding>(
                inflater, R.layout.fragment_people_details, container, false).apply {
            vm = personDetailViewModel
            setLifecycleOwner(this@PeopleDetailFragment)

        }


        /*val binding = DataBindingUtil.inflate<FragmentPeopleDetailsBinding>(
                inflater, R.layout.fragment_people_details, container, false).apply {
            vm = personDetailViewModel
            setLifecycleOwner(this@PeopleDetailFragment)

        }
*/
        vm.person.observe(this, Observer { person->
            Snackbar.make(binding.root, "Person", Snackbar.LENGTH_LONG).show()
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
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.ClientDetail)
    }

}
