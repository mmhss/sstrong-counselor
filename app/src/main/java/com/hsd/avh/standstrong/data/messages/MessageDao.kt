package com.hsd.avh.standstrong.data.awards

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.messages.Message

/**
 * The Data Access Object for the [Award] class.
 */
@Dao
interface MessageDao {
    @Query("SELECT * FROM messages")
    fun getMessages(): LiveData<List<Message>>

    @Query("SELECT * FROM messages WHERE id = :rowId")
    fun getMessageById(rowId: Long): Message


    @Query("SELECT * FROM messages WHERE thread = :postId")
    fun getMessageByPostId(postId: Int): LiveData<List<Message>>


    @Query("SELECT * FROM messages WHERE mother_id = :motherId")
    fun getMessagesForMotherId(motherId: Int): LiveData<Message>

    @Query("SELECT count(*) FROM posts WHERE person_id = :personId")
    fun getMessageCountForPerson(personId: String): LiveData<Int>

    @Query("UPDATE messages SET thread = :threadId WHERE id = :messageId")
    fun updateThreadId(threadId: Int, messageId:Long): Int


    @Insert
    fun insertMessage(message: Message): Long

    @Delete
    fun deleteMessage(message: Message)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<Message>)
}