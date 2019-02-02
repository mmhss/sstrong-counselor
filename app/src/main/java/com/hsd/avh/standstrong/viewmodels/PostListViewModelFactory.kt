package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.hsd.avh.standstrong.data.posts.PostRepository

/**
 * Factory for creating a [PostListViewModel] with a constructor that takes a [PostRepository].
 */
class PostListViewModelFactory(
    private val repository: PostRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = PostListViewModel(repository) as T
}
