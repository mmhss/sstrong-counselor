package com.hsd.avh.standstrong.data.posts

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.people.Person

/**
 * The Data Access Object for the Post class.
 */
@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY date")
    fun getPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPost(postId: String): LiveData<Post>

    @Query("SELECT * FROM posts WHERE person_id = :personId")
    fun getPostsForPerson(personId: String): LiveData<Post>

    @Insert
    fun insertPost(post: Post): Long

    @Delete
    fun deletePost(post: Post)

    @Query("DELETE FROM posts")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)
}
