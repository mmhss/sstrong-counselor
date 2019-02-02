package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.awards.AwardRepository
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository

/**
 * The ViewModel for [AwardFragment].
 */
class AwardViewModel internal constructor(
    private val awardRepository: AwardRepository
) : ViewModel() {

    private val awardList = MediatorLiveData<List<Award>>()

    init {
        awardRepository.refreshAwardList()
        awardList.addSource(awardRepository.getAwards() , awardList::setValue)
    }

    fun getAwards() = awardList

    fun updateAwardList() {
        awardRepository.refreshAwardList()
    }

    companion object {

    }
}
