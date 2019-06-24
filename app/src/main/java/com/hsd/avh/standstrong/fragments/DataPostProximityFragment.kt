package com.hsd.avh.standstrong.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.databinding.*
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.*
import android.graphics.Color
import android.view.animation.DecelerateInterpolator
import com.db.chart.animation.Animation
import com.db.chart.model.BarSet
import kotlinx.android.synthetic.main.fragment_data_proximity.view.*
import android.util.Log
import androidx.lifecycle.Observer
import com.db.chart.renderer.AxisRenderer
import com.hsd.avh.standstrong.adapters.BindingAdapters
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.Const
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DataPostProximityFragment : BaseFragment() {

    //https://github.com/diogobernardino/WilliamChart
    private lateinit var vm: DataPostViewModel
    private val TAG = javaClass.simpleName

    /* private val mLabels = arrayOf("8AM", "9AM","10AM","11AM","12AM", "1PM","2PM","3PM","4PM", "5PM")

     private val mValues = arrayOf(floatArrayOf(1f, 0f, 0f, 1f, 1f, 1f, 1f, 0f, 0f, 0f),
             floatArrayOf(0f, -1f, -1f, 0f, 0f, 0f, 0f, -1f, -1f, -1f))
 */


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


        val binding = DataBindingUtil.inflate<FragmentDataProximityBinding>(
            inflater, R.layout.fragment_data_proximity, container, false).apply {
            vm = dataPostViewModel
            dateString = BindingAdapters.provideNepaliString(Date(postDate))
            setLifecycleOwner(this@DataPostProximityFragment)
        }




        /*binding.root.chart.setAxisBorderValues((-1).toFloat(), 1F, 1F)
                .show(Animation().setInterpolator(DecelerateInterpolator())) //.withEndAction(action)
*/


        vm.getProximityData().observe(viewLifecycleOwner, Observer { data->
            if (!data.isNullOrEmpty()) {

                /* private val mLabels = arrayOf("8AM", "9AM","10AM","11AM","12AM", "1PM","2PM","3PM","4PM", "5PM")

 private val mValues = arrayOf(floatArrayOf(1f, 0f, 0f, 1f, 1f, 1f, 1f, 0f, 0f, 0f),
         floatArrayOf(0f, -1f, -1f, 0f, 0f, 0f, 0f, -1f, -1f, -1f))
*/

                val allLabels = arrayOf("4AM","5AM","6AM","7AM","8AM", "9AM","10AM","11AM","12PM", "1PM","2PM","3PM","4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM")


                val dFormat = SimpleDateFormat("ha")
                //var mValues : Array<FloatArray> = Array<FloatArray>(2,2)

                var togetherPoints :FloatArray = FloatArray(19)
                var apartPoints :FloatArray = FloatArray(19)
                var mLabels : ArrayList<String> = ArrayList<String>()
                var index = 0

                for (label in allLabels) {
                    var found = false;
                    for (d in data) {
                        var hour = dFormat.format(d.chartDate);
                        hour = hour.replace(".", "");
                        if(hour.equals(label,true)) {
                            togetherPoints[index] = d.chartValue!!.toFloat()
                            if (togetherPoints[index] == 1f) {
                                apartPoints[index] = 0f
                            } else {
                                apartPoints[index] = -1f
                            }
                            found = true
                        }
                    }
                    if(!found) {
                        togetherPoints[index] = 0f
                        apartPoints[index] = 0f

                    }
                    index++
                }
                //val array = arrayOfNulls<String>(mLabels.size)
                val mValues = arrayOf(togetherPoints,apartPoints)
                var dataset = BarSet(allLabels, mValues[0])
                dataset.color = Color.parseColor("#f9683a")
                binding.root.chart.addData(dataset)

                dataset = BarSet(allLabels, mValues[1])
                dataset.color = Color.parseColor("#870000")
                binding.root.chart.addData(dataset)

                binding.root.chart.setAxisBorderValues((-1).toFloat(), 1F, 1F)
                    .show()
//                    .show(Animation().setInterpolator(DecelerateInterpolator())) //.withEndAction(action)

                binding.root.chart.setXLabels(AxisRenderer.LabelPosition.OUTSIDE)
                    .show()
//                    .show(Animation().setInterpolator(DecelerateInterpolator()))

            }
        })


        initPerson(motherId).observe(this, Observer {

            binding.person = it
        })


        return binding.root
    }

}
