package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository

/**
 * The ViewModel for [PostListFragment].
 */
class PostListViewModel internal constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val growZoneNumber = MutableLiveData<Int>()

    private val postList = MediatorLiveData<List<Post>>()

    init {
        growZoneNumber.value = NO_GROW_ZONE

        val livePostList = Transformations.switchMap(growZoneNumber) {
            if (it == NO_GROW_ZONE) {
                postRepository.getPosts()
            } else {
                postRepository.getPosts() //postRepository.getPostsWithGrowZoneNumber(it)
            }
        }
        postList.addSource(livePostList, postList::setValue)
    }

    fun getPosts() = postList

    fun setGrowZoneNumber(num: Int) {
        growZoneNumber.value = num
    }

    fun clearGrowZoneNumber() {
        growZoneNumber.value = NO_GROW_ZONE
    }

    fun isFiltered() = growZoneNumber.value != NO_GROW_ZONE

    companion object {
        private const val NO_GROW_ZONE = -1
    }
}
