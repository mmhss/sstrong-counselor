package com.hsd.avh.standstrong.data.posts

import android.net.ProxyInfo
import com.crashlytics.android.Crashlytics
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.utilities.SSUtils
import com.hsd.avh.standstrong.workers.MyAPIException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository module for handling data operations.
 */
class PostRepository private constructor(
        private val postDao: PostDao,
        private val gpsDao: GPSDao,
        private val activityDao: ActivityDao,
        private val proximityDao: ProximityDao
) {

    fun getPosts() = postDao.getPosts()

    fun getPost(postId: Int) = postDao.getPost(postId)

    fun refreshPostList() {
       SSUtils.checkForNewPosts()
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PostRepository? = null

        fun getInstance(postDao: PostDao,gpsDao: GPSDao,activityDao:ActivityDao,proximityDao : ProximityDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: PostRepository(postDao,gpsDao,activityDao,proximityDao).also { instance = it }
                }
    }
}
