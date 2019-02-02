package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsd.avh.standstrong.data.people.PersonRepository

import com.hsd.avh.standstrong.data.posts.PostRepository

/**
 * Factory for creating a [PersonDetailViewModel] with a constructor that takes a [PersonRepository].
 */
class PersonDetailViewModelFactory(
    private val repository: PersonRepository,
    private val personId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = PersonDetailViewModel(repository,personId) as T
}
