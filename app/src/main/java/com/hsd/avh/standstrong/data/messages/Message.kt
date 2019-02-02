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
@Entity(
    tableName = "messages",
    foreignKeys = [ForeignKey(entity = Person::class, parentColumns = ["id"], childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE)],
    indices = [Index("person_id")]
)
data class Message(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "person_id") val personId: String,
        @ColumnInfo(name = "associated_id") val associatedId: String,
        @ColumnInfo(name = "msg") val msg: String,
        @ColumnInfo(name = "type") val msgType: Int,
        @ColumnInfo(name = "submitted_date") val msgDate: Calendar = Calendar.getInstance(),
        @ColumnInfo(name = "sent") val sent: Boolean,
        @ColumnInfo(name = "has_media") val hasMedia: Boolean,
        @ColumnInfo(name = "medial_url") val mediaUrl: String
) {

    override fun toString() = id
}

