package com.hsd.avh.standstrong.workers

import android.content.Context
import androidx.work.*
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.utilities.SSUtils



class MessagesWorker(context : Context, params : WorkerParameters)
    : Worker(context, params) {


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