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
    @Query("SELECT * FROM posts ORDER BY date desc")
    fun getPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE type=2 ORDER BY date desc")
    fun getRAPosts(): LiveData<List<Post>>



    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPost(postId: Int): LiveData<Post>


    //@Query("SELECT * FROM posts WHERE type=2 ORDER BY date desc")
    //fun getRAPost(postId: Int): LiveData<Post>


    /*@Query("SELECT id FROM posts WHERE id = :postId")
    fun getPostId(postId: String): Int
*/

    @Query("SELECT * FROM posts WHERE person_id = :personId")
    fun getPostsForPerson(personId: String): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE type=2 AND person_id = :personId")
    fun getRAPostsForPerson(personId: String): LiveData<List<Post>>


    @Query("SELECT count(*) FROM posts WHERE person_id = :personId")
    fun getPostCountForPerson(personId: String): LiveData<Int>


    @Query("UPDATE posts SET liked = :heart WHERE id = :postId")
    fun updateLiked(postId: Int, heart: Boolean): Int

    @Query("UPDATE posts SET comment_count = comment_count + 1 WHERE id = :postId")
    fun updateCommentCount(postId: Int): Int


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPost(post: Post): Long

    @Delete
    fun deletePost(post: Post)

    @Query("DELETE FROM posts")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)
}
