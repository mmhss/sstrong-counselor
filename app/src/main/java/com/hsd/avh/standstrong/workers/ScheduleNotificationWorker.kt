package com.hsd.avh.standstrong.workers

import android.content.Context
import android.util.Log
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

        Log.d("Worker", "dataload init")

        queryAPIForNewData()
        setupNextScheduledNotification()
        return Result.success()

    }

    private fun queryAPIForNewData(){

        getPeople()
        getAwards()
        getPosts()
    }

    private fun getPosts() {
        SSUtils.checkForNewPosts()
    }

    private fun getPeople() {
        SSUtils.checkForNewPeople()
    }

    private fun getAwards() {
        SSUtils.getAllAwards()
    }

    private fun setupNextScheduledNotification(){
        val notificationWork = OneTimeWorkRequest.Builder(ScheduleNotificationWorker::class.java)
                .setInitialDelay(StandStrong.NEW_DATA_CHECK_FREQUENCY_DAYS.toLong(),TimeUnit.HOURS)
                .addTag(StandStrong.TAG_SCHEDULE)
                .build()
        WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG_SCHEDULE, ExistingWorkPolicy.REPLACE ,notificationWork)
    }



}