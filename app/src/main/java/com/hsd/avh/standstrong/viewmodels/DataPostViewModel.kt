package com.hsd.avh.standstrong.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.*
import org.jetbrains.anko.doAsync
import java.text.SimpleDateFormat
import java.util.*



class DataPostViewModel(
        val postRepository: PostRepository,
        val motherId: Int,
        val pDate: Long
) : ViewModel() {

    var postDate = pDate

    private val activityData = MediatorLiveData<List<Activity>>()
    private val proximityData = MediatorLiveData<List<Proximity>>()
    private val gpsData = MediatorLiveData<List<Gps>>()
    private val personLD = MutableLiveData<Person>()
    private val proximityLD = MutableLiveData<List<Proximity>>()

    init {

        val sDate: Long = setTime(pDate,0,0,0)
        val eDate: Long  = setTime(pDate,23,59,59)

        val liveActivityData = postRepository.getActivityByDate(motherId, StandStrong.ACTIVITY_CONFIDENCE,sDate,eDate)
        val liveGpsData = postRepository.getGpsByDate(motherId, StandStrong.GPS_ACCURACY,sDate,eDate)
        val liveProximityData = postRepository.getProximityByDate(motherId,sDate,eDate)


        activityData.addSource(liveActivityData, activityData::setValue)
        gpsData.addSource(liveGpsData, gpsData::setValue)
        proximityData.addSource(liveProximityData, proximityData::setValue)

    }

    fun getActivityData() = activityData

    fun getProximityData() : LiveData<List<Proximity>> {

        loadProximity()
        return proximityLD
    }

    private fun loadProximity() {

        doAsync {

            val sDate: Long = setTime(pDate,0,0,0)
            val eDate: Long  = setTime(pDate,23,59,59)

            proximityLD.postValue(postRepository.getProximityByDateSync(motherId,sDate,eDate))
        }
    }

    fun getGPSData() = gpsData


    private fun setTime(dateInMill: Long,h:Int,m:Int,s:Int): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = dateInMill
        cal.set(Calendar.HOUR_OF_DAY, h)
        cal.set(Calendar.MINUTE, m)
        cal.set(Calendar.SECOND, s)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getPersonByMotherId() {

        doAsync {


        }
    }
}
