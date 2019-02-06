package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsd.avh.standstrong.data.people.PersonRepository

/**
 * Factory for creating a [PeopleViewModel] with a constructor that takes a [PersonRepository].
 */
class PeopleViewModelFactory(
    private val repository: PersonRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = PeopleViewModel(repository) as T
}
