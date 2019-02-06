package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.*
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository


class PeopleViewModel internal constructor(
    private val peopleRepository: PersonRepository
) : ViewModel() {

    private val peopleList = MediatorLiveData<List<Person>>()
    val hasPeople: LiveData<Boolean>

    init {
        peopleRepository.refreshPersonList()
        peopleList.addSource(peopleRepository.getAllPeople(),peopleList::setValue)
        hasPeople = Transformations.map(peopleList) {
            it != null
        }

    }

    fun getPeople() = peopleList

    fun updatePeople() {
        peopleRepository.refreshPersonList()
    }

}
