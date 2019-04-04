package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.*
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.awards.AwardRepository


class AwardViewModel internal constructor(
    private val awardRepository: AwardRepository
) : ViewModel() {

    fun updateAwardList() {
        awardRepository.refreshAwardList()
    }

    fun subscribeOnAwards(): LiveData<List<Award>> {

        return awardRepository.getAwards()
    }
}
