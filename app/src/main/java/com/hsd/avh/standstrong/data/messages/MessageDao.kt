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

    @Query("SELECT * FROM messages WHERE id = :msgId")
    fun getMessageById(msgId: String): LiveData<Message>

    @Query("SELECT * FROM messages WHERE person_id = :personId")
    fun getMessagesForPerson(personId: String): LiveData<Message>

    @Insert
    fun insertAward(message: Message): Long

    @Delete
    fun deleteAward(message: Message)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<Message>)
}