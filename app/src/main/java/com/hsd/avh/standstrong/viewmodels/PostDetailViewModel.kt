package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository

/**
 * The ViewModel used in [PostDetailFragment].
 */
class PostDetailViewModel(
        postRepository: PostRepository,
        private val postId: Int
) : ViewModel() {

    val post: LiveData<Post> = postRepository.getPost(postId)

}
