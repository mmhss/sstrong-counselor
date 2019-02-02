package com.hsd.avh.standstrong.data.posts

/**
 * Repository module for handling data operations.
 */
class PostRepository private constructor(private val postDao: PostDao) {

    fun getPosts() = postDao.getPosts()

    fun getPost(postId: String) = postDao.getPost(postId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PostRepository? = null

        fun getInstance(postDao: PostDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: PostRepository(postDao).also { instance = it }
                }
    }
}
