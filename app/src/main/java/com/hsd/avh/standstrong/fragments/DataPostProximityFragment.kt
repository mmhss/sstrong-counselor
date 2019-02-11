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
import java.text.SimpleDateFormat


class DataPostProximityFragment : Fragment() {

    //https://github.com/diogobernardino/WilliamChart
    private lateinit var vm: DataPostViewModel

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
                val dFormat = SimpleDateFormat("ha")
                //var mValues : Array<FloatArray> = Array<FloatArray>(2,2)

                var togetherPoints :FloatArray = FloatArray(data.size)
                var apartPoints :FloatArray = FloatArray(data.size)
                var mLabels : ArrayList<String> = ArrayList<String>()
                var index : Int = 0
                for (d in data) {
                    mLabels.add(dFormat.format(d.chartDate))
                    //togetherPoints[index] = d.chartValue!!.toFloat()

                    if(Math.random() < 0.5)
                        togetherPoints[index] = 0f
                    else
                        togetherPoints[index] = 1f

                    if(togetherPoints[index] == 1f) {
                        apartPoints[index] = 0f
                    } else {
                        apartPoints[index] = -1f
                    }
                    index++
                }

                val array = arrayOfNulls<String>(mLabels.size)
                val mValues = arrayOf(togetherPoints,apartPoints)
                var dataset = BarSet(mLabels.toArray(array), mValues[0])
                dataset.color = Color.parseColor("#f9683a")
                binding.root.chart.addData(dataset)

                dataset = BarSet(mLabels.toArray(array), mValues[1])
                dataset.color = Color.parseColor("#870000")
                binding.root.chart.addData(dataset)

                binding.root.chart.setAxisBorderValues((-1).toFloat(), 1F, 1F)
                        .show(Animation().setInterpolator(DecelerateInterpolator())) //.withEndAction(action)

                binding.root.chart.setXLabels(AxisRenderer.LabelPosition.OUTSIDE)
                        .show(Animation().setInterpolator(DecelerateInterpolator()))

            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //StandStrong.firebaseInstance().setCurrentScreen(this!!.activity!!, activity?.javaClass?.simpleName, activity?.javaClass?.simpleName);
        FirebaseTrackingUtil(activity!!).track(FirebaseTrackingUtil.Screens.ProximityData)
    }
}
