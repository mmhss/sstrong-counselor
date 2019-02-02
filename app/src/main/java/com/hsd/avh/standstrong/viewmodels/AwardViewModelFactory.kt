package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsd.avh.standstrong.data.awards.AwardRepository
import com.hsd.avh.standstrong.data.people.PersonRepository

import com.hsd.avh.standstrong.data.posts.PostRepository

/**
 * Factory for creating a [AwardViewModel] with a constructor that takes a [AwardRepository].
 */
class AwardViewModelFactory(
    private val repository: AwardRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = AwardViewModel(repository) as T
}
