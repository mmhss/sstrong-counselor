package com.hsd.avh.standstrong.data.awards

import com.hsd.avh.standstrong.data.messages.Message
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class MessageRepository private constructor(
    private val messageDao: MessageDao
) {

    suspend fun createMessage(msg: Message) {
        withContext(IO) {
            messageDao.insertMessage(msg)


        }
    }

    suspend fun removeAward(msg: Message) {
        withContext(IO) {
            messageDao.deleteMessage(msg)
        }
    }

    fun getMessagesByPersonId(msgId: Int) =
            messageDao.getMessagesForMotherId(msgId)

    fun getMessageByPostId(postId: Int) = messageDao.getMessageByPostId(postId) //getMessages()

    fun getMessagesByPerson(motherId: Int) = messageDao.getMessagesForMotherId(motherId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: MessageRepository? = null

        fun getInstance(messageDao: MessageDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: MessageRepository(messageDao).also { instance = it }
                }
    }
}