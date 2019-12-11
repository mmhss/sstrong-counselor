package com.hsd.avh.standstrong.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hsd.avh.standstrong.adapters.PeopleAdapter
import com.hsd.avh.standstrong.databinding.FragmentPeopleBinding
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.PeopleViewModel

class PeopleFragment : BaseFragment() {

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


        val adapter = PeopleAdapter(analyticsManager)
        binding.peopleList.adapter = adapter

        val swipeRefreshLayout =  binding.swiping
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.updatePeople()
            //Just a hack with no error or success being returned.
            swipeRefreshLayout.postDelayed({

                analyticsManager.trackEvent("Refresh of people")
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
        viewModel.updatePeople()
    }

}
