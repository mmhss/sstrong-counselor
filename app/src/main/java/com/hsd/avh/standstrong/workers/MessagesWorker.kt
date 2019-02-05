package com.hsd.avh.standstrong.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.crashlytics.android.Crashlytics
import java.util.*
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.api.ApiEndpoints
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.data.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.utilities.SSUtils
import kotlinx.coroutines.*


class MessagesWorker(context : Context, params : WorkerParameters)
    : Worker(context, params) {


    companion object {
        private const val TAG = "SSMessages"

    }
    val database = AppDatabase.getInstance(applicationContext)


    override fun doWork(): Result {

        //val mRepository : MessageRepository.Companion
        //.. Companion.getInstance(AppDatabase.getInstance(context).messageDao())

        val id = inputData.getLong(StandStrong.MESSAGE_ROW_ID,-1)
        uploadNewMessages(id)
        queryAPIForNewMessages()
        return Result.success()

    }

    private fun uploadNewMessages(id: Long){
        SSUtils.uploadMessage(id)
    }


    private fun queryAPIForNewMessages(){
       SSUtils.checkForNewMessages()
    }


}