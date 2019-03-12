package com.hsd.avh.standstrong.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.data.awards.MessageRepository
import com.hsd.avh.standstrong.data.messages.Message
import androidx.databinding.ObservableField
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.workers.MessagesWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit



class MessageViewModel internal constructor(
    private val messageRepository: MessageRepository,
    private val motherId: Int,
    private val postId: Int
) : ViewModel() {

    //private val growZoneNumber = MutableLiveData<Int>()

    private val messageList = MediatorLiveData<List<Message>>()
    val txtMessage = ObservableField<String>()

    val database = AppDatabase.getInstance(StandStrong.applicationContext())

    init {
        val liveMessageList = messageRepository.getMessageByPostId(postId)
        messageList.addSource(liveMessageList, messageList::setValue)
    }

    fun getMessages() = messageList

    fun addMessage(){
        if(!txtMessage.get().toString().isNullOrEmpty()) {
            val txtMsg = txtMessage.get().toString()
            txtMessage.set("")
            CoroutineScope(Dispatchers.IO).launch {
                val m = Message(motherId,
                        txtMsg, StandStrong.MESSAGE_DIRECTION_OUT,
                        postId,
                        Date()
                )

                val id = database.messageDao().insertMessage(m)
                database.postDao().updateCommentCount(postId)

                //Now upload using WorkManager (Also Checks for new messages)
                val inputData = Data.Builder().putLong(StandStrong.MESSAGE_ROW_ID, id).build()
                val notificationWork = OneTimeWorkRequest.Builder(MessagesWorker::class.java)
                        .setInitialDelay(1, TimeUnit.SECONDS)
                        .addTag(StandStrong.TAG_MSG)
                        .setInputData(inputData)
                        .build()
                WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG_MSG, ExistingWorkPolicy.REPLACE, notificationWork);
            }
        }
    }




}
