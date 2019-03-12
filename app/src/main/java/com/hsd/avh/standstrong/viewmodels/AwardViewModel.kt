package com.hsd.avh.standstrong.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.awards.AwardRepository
import kotlinx.coroutines.runBlocking


class AwardViewModel internal constructor(
    private val awardRepository: AwardRepository
) : ViewModel() {

    private val awardList = MediatorLiveData<List<Award>>()
    val hasAwards: LiveData<Boolean>

    init {
        //awardRepository.refreshAwardList()

        awardList.addSource(awardRepository.getAwards() , awardList::setValue)
        hasAwards = Transformations.map(awardList) {
            it != null
        }
    }

    fun getAwards() = awardList

    fun updateAwardList() {
        awardRepository.refreshAwardList()
    }
}
