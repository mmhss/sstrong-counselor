package com.hsd.avh.standstrong.fragments



import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.adapters.BindingAdapters
import com.hsd.avh.standstrong.databinding.*
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.Const
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.*
import kotlinx.android.synthetic.main.fragment_data_activity.view.*
import java.text.SimpleDateFormat
import java.util.*

class DataPostActivityFragment : BaseFragment() {

    private lateinit var vm: DataPostViewModel
    private val TAG = javaClass.canonicalName
    lateinit var binding: FragmentDataActivityBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //Post Id is
        val motherId = DataPostActivityFragmentArgs.fromBundle(arguments).motherId
        val postDate = DataPostActivityFragmentArgs.fromBundle(arguments).postDate
        val factory = InjectorUtils.providePostDataViewModelFactory(requireActivity(), motherId,postDate)
        vm = ViewModelProviders.of(this, factory)
                .get(DataPostViewModel::class.java)

        val dataPostViewModel = ViewModelProviders.of(this, factory)
                .get(DataPostViewModel::class.java)


        binding = DataBindingUtil.inflate<FragmentDataActivityBinding>(
                inflater, R.layout.fragment_data_activity, container, false).apply {
            dateString = BindingAdapters.provideNepaliString(Date(postDate))
            setLifecycleOwner(this@DataPostActivityFragment)
        }
        val res : Resources = StandStrong.applicationContext().resources
        binding.root.arc_chart_view.setSectionIcons(mutableListOf(
                BitmapFactory.decodeResource(res,R.drawable.activity_bicycle), //3 but 1 for data element
                BitmapFactory.decodeResource(res,R.drawable.activity_running),
                BitmapFactory.decodeResource(res,R.drawable.activity_standing),
                BitmapFactory.decodeResource(res,R.drawable.activity_tilting),
                BitmapFactory.decodeResource(res,R.drawable.activity_unknown),
                BitmapFactory.decodeResource(res,R.drawable.activity_vehicle),  //1
                BitmapFactory.decodeResource(res,R.drawable.activity_walking) //last data element
        ))

        /*vm.post.observe(this, Observer { post->
            Snackbar.make(binding.root, "Post", Snackbar.LENGTH_LONG).show()
        })*/

        binding.root.arc_chart_view.setSectionValue(0,0)
        binding.root.arc_chart_view.setSectionValue(1,0)
        binding.root.arc_chart_view.setSectionValue(2,0)
        binding.root.arc_chart_view.setSectionValue(3,0)
        binding.root.arc_chart_view.setSectionValue(4,0)
        binding.root.arc_chart_view.setSectionValue(5,0)
        binding.root.arc_chart_view.setSectionValue(6,0)


        vm.getActivityData().observe(viewLifecycleOwner, Observer { data->
            if (data != null) {
                val byActivity = data.groupBy { it.activityType }
                var total = 0

                Log.d(TAG, "by ${byActivity.keys}")
                Log.d(TAG, "by vals ${byActivity.values}")

                byActivity.forEach { (key, value) -> total += value.size }

                byActivity.forEach { (key, value) ->

                    var rings = ((value.size.toFloat() / total.toFloat())*10f).toInt()

                    when (key) {
                        StandStrong.ACTIVITY_BICYCLE -> binding.root.arc_chart_view.setSectionValue(0,rings)
                        StandStrong.ACTIVITY_RUNNING -> binding.root.arc_chart_view.setSectionValue(1,rings)
                        StandStrong.ACTIVITY_STILL -> binding.root.arc_chart_view.setSectionValue(2,rings)
                        StandStrong.ACTIVITY_TILTING -> binding.root.arc_chart_view.setSectionValue(3,rings)
                        StandStrong.ACTIVITY_UNKNOWN -> binding.root.arc_chart_view.setSectionValue(4,rings)
                        StandStrong.ACTIVITY_VEHICLE -> binding.root.arc_chart_view.setSectionValue(5,rings)
                        StandStrong.ACTIVITY_FOOT -> binding.root.arc_chart_view.setSectionValue(6,rings)
                        else -> "na"
                    }
                }
            }
        })

        initPerson(motherId).observe(this, Observer {

            binding.person = it
        })

        return binding.root
    }
}
