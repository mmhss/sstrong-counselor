package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsd.avh.standstrong.data.people.PersonRepository

import com.hsd.avh.standstrong.data.posts.PostRepository

/**
 * Factory for creating a [PostDetailViewModel] with a constructor that takes a [PostRepository].
 */
class PostDetailViewModelFactory(
    private val repository: PostRepository,
    private val postId: Int
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = PostDetailViewModel(repository,postId) as T
}
