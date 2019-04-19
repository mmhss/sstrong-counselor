package com.hsd.avh.standstrong.fragments.baseFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.managers.AnalyticsManager
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as StandStrong).managerComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()

        analyticsManager.trackScreen(javaClass.simpleName, activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }


}
