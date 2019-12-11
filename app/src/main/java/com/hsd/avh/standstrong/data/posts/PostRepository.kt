package com.hsd.avh.standstrong.data.posts

import android.net.ProxyInfo
import androidx.collection.ArrayMap
import androidx.paging.DataSource
import androidx.sqlite.db.SimpleSQLiteQuery
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
import java.util.*

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

    fun getRAPosts() = postDao.getRAPosts()

    fun getAllPaged(): DataSource.Factory<Int, Post> {
        return postDao.getAllPaged()
    }

    fun getFilteredPagedPosts(sql:SimpleSQLiteQuery) = postDao.getFilteredPostsPaged(sql)

    fun getRAPostsPaged() = postDao.getRAPostsPaged()

    //fun getFilteredPosts(filter: List<Int>) =  postDao.getFilteredPosts(filter)

    fun getFilteredPosts(sql:SimpleSQLiteQuery) =  postDao.getFilteredPosts(sql)

    fun getPost(postId: Int) = postDao.getPost(postId)

    //fun getRAPost(postId: Int) = postDao.getRAPost(postId)

    //Activity
    fun getActivityByDate(motherId:Int,confidence:Int,stDate:Long,endDate: Long) = activityDao.getActivityByDate(motherId,confidence,stDate,endDate)
    fun getAllActivity() = activityDao.getActivity()

    //Proximity
    fun getProximityByDate(motherId:Int,stDate:Long,endDate: Long) = proximityDao.getProximityByDate(motherId,stDate,endDate)
    //Gps
    fun getGpsByDate(motherId:Int,confidence:Int,stDate:Long,endDate: Long) = gpsDao.getGpsByDate(motherId,confidence,stDate,endDate)


    fun refreshPostList() {
       SSUtils.checkForNewPosts()
    }

    fun refreshMessagesList() {
        SSUtils.checkForNewMessages()
    }

    fun getProximityByDateSync(motherId: Int, sDate: Long, eDate: Long) = proximityDao.getProximityByDateSync(motherId,sDate,eDate)

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
