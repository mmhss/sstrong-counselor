package com.hsd.avh.standstrong.workers

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.utilities.SSUtils
import com.hsd.avh.standstrong.R
import kotlinx.coroutines.*


class ScheduleNotificationWorker(context : Context, params : WorkerParameters)
    : Worker(context, params) {


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
        val cardTitle = StandStrong.applicationContext().getString(R.string.card_title_education)
        val mediaUrl = "@drawable/"+SSUtils.getNextEducationalPost()
        val avatarUrl = "https://www.tinygraphs.com/squares/Education?theme=heatwave&numcolors=4&size=50&fmt=png"
        val postTitle = SSUtils.getEducationalPostTitle(SSUtils.getNextEducationalPost())
        val postTxt =SSUtils.getEducationalPostText(SSUtils.getNextEducationalPost())
        val p: Post = Post( "All",99999,Date(),avatarUrl,cardTitle,"",mediaUrl,false,0,1,postTitle,postTxt)

        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){
                database.postDao().insertPost(p)
            }
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
        WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG, ExistingWorkPolicy.REPLACE ,notificationWork)
    }



}