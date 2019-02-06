package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hsd.avh.standstrong.data.awards.MessageRepository

/**
 * Factory for creating a [MessageViewModel] with a constructor that takes a [MessageRepository].
 */
class MessageViewModelFactory(
    private val repository: MessageRepository,
    private val motherId: Int,
    private val postId: Int
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MessageViewModel(repository,motherId,postId) as T
}
