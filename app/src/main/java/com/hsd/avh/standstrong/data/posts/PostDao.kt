package com.hsd.avh.standstrong.data.posts

import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.people.Person
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.room.RawQuery





/**
 * The Data Access Object for the Post class.
 */
@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY date desc")
    fun getPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE type=2 ORDER BY date desc")
    fun getRAPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts ORDER BY date desc")
    fun getAllPaged(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM posts WHERE type=2 ORDER BY date desc")
    fun getRAPostsPaged(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPost(postId: Int): LiveData<Post>

/*
    @Query("SELECT * FROM posts WHERE type IN (:filterValues) ORDER BY date DESC")
    fun getFilteredPosts(filterValues:List<Int>) : LiveData<List<Post>>
*/

    @RawQuery(observedEntities = [Post::class])
    fun getFilteredPosts(query: SupportSQLiteQuery): LiveData<List<Post>>

    @RawQuery(observedEntities = [Post::class])
    fun getFilteredPostsPaged(query: SupportSQLiteQuery): DataSource.Factory<Int, Post>

    //@Query("SELECT * FROM posts WHERE type=2 ORDER BY date desc")
    //fun getRAPost(postId: Int): LiveData<Post>


    /*@Query("SELECT id FROM posts WHERE id = :postId")
    fun getPostId(postId: String): Int
*/

    @Query("SELECT * FROM posts WHERE person_id = :personId order by date desc")
    fun getPostsForPerson(personId: String): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE person_id = :personId order by date desc")
    fun getPostsForPersonPaged(personId: String): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM posts WHERE type=2 AND person_id = :personId order by date desc")
    fun getRAPostsForPerson(personId: String): LiveData<List<Post>>


    @Query("SELECT count(*) FROM posts WHERE person_id = :personId")
    fun getPostCountForPerson(personId: String): LiveData<Int>


    @Query("UPDATE posts SET liked = :heart WHERE id = :postId")
    fun updateLiked(postId: Int, heart: Boolean): Int

    @Query("UPDATE posts SET comment_count = comment_count + 1 WHERE id = :postId")
    fun updateCommentCount(postId: Int): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post): Long

    @Delete
    fun deletePost(post: Post)

    @Query("DELETE FROM posts")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)

    @Query("SELECT * FROM posts WHERE type=2 AND person_id = :personId order by date desc")
    fun getRAPostsForPersonPaged(personId: String): DataSource.Factory<Int, Post>
}
