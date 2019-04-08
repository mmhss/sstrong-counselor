package com.hsd.avh.standstrong.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.adapters.PeopleAdapter
import com.hsd.avh.standstrong.databinding.FragmentPeopleBinding
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.PeopleViewModel

class PeopleFragment : Fragment() {

    private lateinit var viewModel: PeopleViewModel
    private lateinit var binding:FragmentPeopleBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        val factory = InjectorUtils.providePersonViewModelFactory(context)
        viewModel = ViewModelProviders.of(this, factory).get(PeopleViewModel::class.java)


        val adapter = PeopleAdapter()
        binding.peopleList.adapter = adapter

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

    private fun subscribeUi(adapter: PeopleAdapter) {

        viewModel.subscribeOnPeople().observe(this, Observer { people ->

            if (people.isNotEmpty()) {

                adapter.submitList(people)
                binding.noPeople.visibility =  View.GONE;
            }
            if (people.isNullOrEmpty()){
                binding.noPeople.visibility =  View.VISIBLE;
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //StandStrong.firebaseInstance().setCurrentScreen(this!!.activity!!, activity?.javaClass?.simpleName, activity?.javaClass?.simpleName);
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.Clients)

        viewModel.updatePeople()
    }

}
