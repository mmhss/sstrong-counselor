package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.PostRepository
import java.util.*

class DataPostViewModelFactory(
        private val repository: PostRepository,
        private val motherId: Int,
        private val postDate: Long

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = DataPostViewModel(repository,motherId,postDate) as T
}
