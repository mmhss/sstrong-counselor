package com.hsd.avh.standstrong.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.awards.AwardRepository
import kotlinx.coroutines.runBlocking


class AwardViewModel internal constructor(
    private val awardRepository: AwardRepository
) : ViewModel() {

    private val awardList = MediatorLiveData<List<Award>>()

    init {
        //awardRepository.refreshAwardList()

        awardList.addSource(awardRepository.getAwards() , awardList::setValue)
    }

    fun getAwards() = awardList

    fun updateAwardList() {
        awardRepository.refreshAwardList()
    }
}
