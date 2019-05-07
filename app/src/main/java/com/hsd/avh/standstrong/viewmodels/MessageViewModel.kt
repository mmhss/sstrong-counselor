package com.hsd.avh.standstrong.viewmodels

import android.util.Log
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
import com.hsd.avh.standstrong.data.messages.ApiMessage
import com.hsd.avh.standstrong.utilities.SSUtils
import com.hsd.avh.standstrong.workers.MessagesWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import java.util.concurrent.TimeUnit



class MessageViewModel internal constructor(
    private val messageRepository: MessageRepository,
    private val motherId: Int,
    private val postId: Int
) : ViewModel() {

    //private val growZoneNumber = MutableLiveData<Int>()

    private val TAG = javaClass.simpleName

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

            doAsync {

                val m = Message(motherId,
                        txtMsg, StandStrong.MESSAGE_DIRECTION_OUT,
                        System.currentTimeMillis(),
                        Date())

                val id = database.messageDao().insertMessage(m)
                database.postDao().updateCommentCount(postId)

                Log.d(TAG, "message added locally $id")

                uiThread {
                    SSUtils.uploadMessage(ApiMessage(m))
                    SSUtils.checkForNewMessages()
                }
            }
        }
    }




}
