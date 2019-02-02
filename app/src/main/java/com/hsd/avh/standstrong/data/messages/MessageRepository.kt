package com.hsd.avh.standstrong.data.awards

import com.hsd.avh.standstrong.data.messages.Message
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class MessageRepository private constructor(
    private val messageDao: MessageDao
) {

    suspend fun createMessage(msg: Message) {
        withContext(IO) {
            messageDao.insertAward(msg)
        }
    }

    suspend fun removeAward(msg: Message) {
        withContext(IO) {
            messageDao.deleteAward(msg)
        }
    }

    fun getMessagesByPersonId(msgId: String) =
            messageDao.getMessagesForPerson(msgId)

    fun getMessages() = messageDao.getMessages()

    fun getMessagesByPerson(personId: String) = messageDao.getMessagesForPerson(personId)

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