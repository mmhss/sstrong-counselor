package com.hsd.avh.standstrong.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.hsd.avh.standstrong.databinding.FragmentDataProximityBinding
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.DataPostViewModel
import kotlinx.android.synthetic.main.fragment_data_proximity.*
import java.text.SimpleDateFormat
import com.hsd.avh.standstrong.data.CustomDataEntry
import com.anychart.enums.LabelsOverlapMode
import android.R.attr.orientation
import android.R.attr.orientation
import com.anychart.enums.ScaleStackMode
import android.R.attr.animation
import android.R.attr.orientation
import android.R.attr.animation
import com.anychart.enums.Orientation


class DataPostProximityFragment : Fragment() {

    //https://github.com/diogobernardino/WilliamChart
    private lateinit var vm: DataPostViewModel

   /* private val mLabels = arrayOf("8AM", "9AM","10AM","11AM","12AM", "1PM","2PM","3PM","4PM", "5PM")

    private val mValues = arrayOf(floatArrayOf(1f, 0f, 0f, 1f, 1f, 1f, 1f, 0f, 0f, 0f),
            floatArrayOf(0f, -1f, -1f, 0f, 0f, 0f, 0f, -1f, -1f, -1f))
*/

    private val TAG = javaClass.canonicalName

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
                inflater, com.hsd.avh.standstrong.R.layout.fragment_data_proximity, container, false).apply {
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

                val allLabels = arrayOf("4AM","5AM","6AM","7AM","8AM", "9AM","10AM","11AM","12PM", "1PM","2PM","3PM","4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM")


                val dFormat = SimpleDateFormat("ha")
                //var mValues : Array<FloatArray> = Array<FloatArray>(2,2)

                var togetherPoints :FloatArray = FloatArray(19)
                var apartPoints :FloatArray = FloatArray(19)
                var mLabels : ArrayList<String> = ArrayList<String>()
                var index = 0

                val seriesData = mutableListOf<CustomDataEntry>()

                for (label in allLabels) {

                    var found = false

                    for (d in data) {

                        if(dFormat.format(d.chartDate) == label) {

                            if (d.chartValue!!.toInt() == 1)
                                seriesData.add(CustomDataEntry(label, 1, 0))
                            else
                                seriesData.add(CustomDataEntry(label, 0, -1))

                            found = true
                            break
                        }
                    }

                    if (!found)
                        seriesData.add(CustomDataEntry(label, 0, 0))
                }

//                val seriesData = ArrayList<Cus>()
//                seriesData.add(CustomDataEntry("Nail polish", 5376, -229))
//                seriesData.add(CustomDataEntry("Eyebrow pencil", 10987, -932))
//                seriesData.add(CustomDataEntry("Rouge", 7624, -5221))
//                seriesData.add(CustomDataEntry("Lipstick", 8814, -256))
//                seriesData.add(CustomDataEntry("Eyeshadows", 8998, -308))
//                seriesData.add(CustomDataEntry("Eyeliner", 9321, -432))
//                seriesData.add(CustomDataEntry("Foundation", 8342, -701))
//                seriesData.add(CustomDataEntry("Lip gloss", 6998, -908))
//                seriesData.add(CustomDataEntry("Mascara", 9261, -712))
//                seriesData.add(CustomDataEntry("Shampoo", 5376, -9229))
//                seriesData.add(CustomDataEntry("Hair conditioner", 10987, -13932))
//                seriesData.add(CustomDataEntry("Body lotion", 7624, -10221))
//                seriesData.add(CustomDataEntry("Shower gel", 8814, -12256))
//                seriesData.add(CustomDataEntry("Soap", 8998, -5308))
//                seriesData.add(CustomDataEntry("Eye fresher", 9321, -432))
//                seriesData.add(CustomDataEntry("Deodorant", 8342, -11701))
//                seriesData.add(CustomDataEntry("Hand cream", 7598, -5808))
//                seriesData.add(CustomDataEntry("Foot cream", 6098, -3987))
//                seriesData.add(CustomDataEntry("Night cream", 6998, -847))
//                seriesData.add(CustomDataEntry("Day cream", 5304, -4008))
//                seriesData.add(CustomDataEntry("Vanila cream", 9261, -712))

                val barChart = AnyChart.bar()

                barChart.yAxis(0.0).enabled(false)

                val set = Set.instantiate()
                set.data(seriesData as List<DataEntry>?)
                val series1Data = set.mapAs("{ x: 'x', value: 'value' }")
                val series2Data = set.mapAs("{ x: 'x', value: 'value2' }")

                val series1 = barChart.bar(series1Data)
                series1.name("Alone")
                    .color("Red")
                series1.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)

                val series2 = barChart.bar(series2Data)
                series2.name("Together").color("Blue")
                series2.tooltip()
                    .position("left")
                    .anchor(Anchor.RIGHT_CENTER)

                barChart.legend().enabled(true)
                barChart.legend().inverted(true)
                barChart.legend().fontSize(13.0)
                barChart.legend().padding(0.0, 0.0, 20.0, 0.0)

                chart.setChart(barChart)
//
//                for (label in allLabels) {
//
//                    var found = false
//                    for (d in data) {
//
//                        Log.d(TAG, "data $d")
//
//                        if(dFormat.format(d.chartDate) == label) {
//                            togetherPoints[index] = d.chartValue!!.toFloat()
//                            if (togetherPoints[index] == 1f) {
//                                apartPoints[index] = 0f
//                            } else {
//                                apartPoints[index] = -1f
//                            }
//                            found = true
//                        }
//                    }
//                    if(!found) {
//                        togetherPoints[index] = 0f
//                        apartPoints[index] = 0f
//                    }
//                    index++
//                }
//
//                //val array = arrayOfNulls<String>(mLabels.size)
//                val mValues = arrayOf(togetherPoints,apartPoints)
//                var dataset = BarSet(allLabels, mValues[0])
//                dataset.color = Color.parseColor("#f9683a")
//                binding.root.chart.addData(dataset)
//
//                dataset = BarSet(allLabels, mValues[1])
//                dataset.color = Color.parseColor("#870000")
//                binding.root.chart.addData(dataset)
//
//                binding.root.chart.setXLabels(AxisRenderer.LabelPosition.OUTSIDE)
//                        .show(Animation().setInterpolator(DecelerateInterpolator()))
//
            }
        })

        return binding.root
    }


    private inner class CustomDataEntry internal constructor(x: String, value: Number, value2: Number) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
        }
    }
}
