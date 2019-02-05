package com.hsd.avh.ema.emaScheduler

import android.content.Context
import androidx.work.*
import com.crashlytics.android.Crashlytics
import java.util.*
import java.util.concurrent.TimeUnit
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.api.ApiEndpoints
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.utilities.SSUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.workers.MyAPIException


class ScheduleNotificationWorker(context : Context, params : WorkerParameters)
    : Worker(context, params) {


    companion object {
        private const val TAG = "SSNotification"

    }
    val database = AppDatabase.getInstance(applicationContext)

    override fun doWork(): Result {

        queryAPIForNewData()
        setupNextScheduledNotification()
        return Result.success()

    }

    private fun queryAPIForNewData(){

        getPeople()
        getAwards()
        getEducationPosts()
        getPosts()
    }

    private fun getPosts() {
        SSUtils.checkForNewPosts()
    }


    private fun getEducationPosts() {
        var cardTitle = StandStrong.applicationContext().getString(R.string.card_title_education)
        var mediaUrl = "@drawable/"+SSUtils.getNextEducationalPost()
        var avatarUrl = "https://www.tinygraphs.com/squares/Education?theme=heatwave&numcolors=4&size=50&fmt=png"
        var postTitle = SSUtils.getEducationalPostTitle(SSUtils.getNextEducationalPost())
        var postTxt =SSUtils.getEducationalPostText(SSUtils.getNextEducationalPost())
        var p: Post = Post( "All",99999,Date(),avatarUrl,cardTitle,"",mediaUrl,false,0,1,postTitle,postTxt)
        CoroutineScope(Dispatchers.IO).launch {
            database.postDao().insertPost(p)
        }

    }


    private fun getPeople() {
        SSUtils.checkForNewPeople()
    }

    private fun getAwards() {
      SSUtils.checkForNewAwards()
    }

    private fun setupNextScheduledNotification(){
        val notificationWork = OneTimeWorkRequest.Builder(ScheduleNotificationWorker::class.java)
                .setInitialDelay(24,TimeUnit.HOURS)
                .addTag(StandStrong.TAG)
                .build()
        WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG, ExistingWorkPolicy.REPLACE ,notificationWork);
    }



}