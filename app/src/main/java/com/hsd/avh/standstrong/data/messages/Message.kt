package com.hsd.avh.standstrong.data.messages

import androidx.room.*
import com.hsd.avh.standstrong.data.people.Person
import java.util.*


enum class PostTypeEnum{
    Post, Award, Education, Message
}


/**
 * [Message] contains all communication between [Person]s including client, counsellor and supervisors
 *
 */
/*foreignKeys = [ForeignKey(entity = Person::class, parentColumns = ["id"], childColumns = ["person_id"],
onDelete = ForeignKey.CASCADE)],*/

@Entity(
    tableName = "messages")
data class Message(
        @ColumnInfo(name = "mother_id") val motherId: Int,
        @ColumnInfo(name = "msg") val msg: String,
        @ColumnInfo(name = "direction") val direction: String,
        @ColumnInfo(name = "thread") val msgThread: Int,
        @ColumnInfo(name = "submitted_date") val msgDate: Calendar = Calendar.getInstance(),
       // @ColumnInfo(name = "sent") val sent: Boolean,
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {

    override fun toString() = id
}

