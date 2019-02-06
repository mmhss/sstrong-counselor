package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository


class PostListViewModel internal constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    //private val growZoneNumber = MutableLiveData<Int>()

    private val postList = MediatorLiveData<List<Post>>()

    init {
        //EACH TIME YOU LOAD VIEWMODEL CHECK FOR NEW POSTS
        //ALSO A SCHEDULED BACKGROUND SERVICE THAT CHECKS DAILY
        postRepository.refreshPostList()
        val livePostList = postRepository.getPosts()
        postList.addSource(livePostList, postList::setValue)
    }

    fun getPosts() = postList

    fun updatePeople() {
        postRepository.refreshPostList()
    }


    companion object {
        //private const val NO_GROW_ZONE = -1
    }
}
