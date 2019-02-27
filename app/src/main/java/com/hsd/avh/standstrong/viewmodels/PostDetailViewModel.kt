package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository

class PostDetailViewModel(
        postRepository: PostRepository,
        postId: Int
) : ViewModel() {

    val post: LiveData<Post> = postRepository.getPost(postId)


}
