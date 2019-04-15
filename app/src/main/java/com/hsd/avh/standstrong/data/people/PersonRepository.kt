package com.hsd.avh.standstrong.data.people


import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.api.ApiEndpoints
import androidx.annotation.NonNull
import android.R.attr.name
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.sqlite.db.SimpleSQLiteQuery
import com.crashlytics.android.Crashlytics
import com.hsd.avh.standstrong.data.awards.AwardDao
import com.hsd.avh.standstrong.data.awards.MessageDao
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostDao
import com.hsd.avh.standstrong.utilities.SSUtils
import com.hsd.avh.standstrong.workers.MyAPIException


class PersonRepository private constructor(
        private val personDao: PersonDao,
        private val postDao: PostDao,
        private val awardDao: AwardDao,
        private val messageDao: MessageDao
) {


    fun getPostListById(postId: String) = postDao.getPostsForPerson(postId)

    fun getRAPostListById(postId: String) = postDao.getRAPostsForPerson(postId)


    fun refreshPersonList() {
        SSUtils.checkForNewPeople()
    }

    suspend fun createPerson(person: Person) {
        withContext(IO) {
            personDao.insertPerson(person)
        }
    }

    suspend fun insertPost(post: Post) : Long {
        return postDao.insertPost(post)
    }

    suspend fun insertMessage(msg: Message) : Long {
        return messageDao.insertMessage(msg)
    }

    suspend fun removePerson(person: Person) {
        withContext(IO) {
            personDao.deletePerson(person)
        }
    }

    fun getPersonById(personId: String) =
            personDao.getPersonByMotherSsId(personId)

    suspend fun  getImmutablePersonById (personId: String) : Person {
        return withContext(IO){
            personDao.getImmutablePersonByMotherSsId(personId)
        }
    }

    fun getPostListByIdAndFilters(sql: SimpleSQLiteQuery) =  postDao.getFilteredPosts(sql)

    fun getPostListByIdAndFiltersPaged(sql: SimpleSQLiteQuery) =  postDao.getFilteredPostsPaged(sql)

    fun getAllPeople() = personDao.getAllPeople()


    fun getPeopleCount() : LiveData<Int> {
            return personDao.getPersonCount()
    }

    fun getAwardCount(personId:String) : LiveData<Int> {
        return awardDao.getAwardCountForPerson(personId)
    }

    fun getPostCount(personId:String) : LiveData<Int> {
        return postDao.getPostCountForPerson(personId)
    }

    fun getMessageCount(personId:String) : LiveData<Int> {
        return messageDao.getMessageCountForPerson(personId)
    }

    fun getAllPeopleList(): List<Person>? {
        return personDao.getAllPeopleList()
    }

    fun getRAPostListByIdPaged(personId: String): DataSource.Factory<Int, Post> {
        return postDao.getRAPostsForPersonPaged(personId)
    }

    fun getPostListByIdPaged(personId: String): DataSource.Factory<Int, Post> {

        return postDao.getPostsForPersonPaged(personId)
    }

    fun getPosts(): DataSource.Factory<Int, Post> {

        return postDao.getAllPaged()
    }


    companion object {


        // For Singleton instantiation
        @Volatile private var instance: PersonRepository? = null

        fun getInstance(personDao: PersonDao,postDao:PostDao,awardDao:AwardDao,messageDao: MessageDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: PersonRepository(personDao,postDao,awardDao,messageDao).also { instance = it }
                }


        }

    }


private fun <T> Call<T>.enqueue(callback: Callback<ApiPerson>) {

}
