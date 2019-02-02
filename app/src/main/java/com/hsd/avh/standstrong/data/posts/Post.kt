package com.hsd.avh.standstrong.data.posts

import androidx.room.*
import com.hsd.avh.standstrong.data.people.Person
import java.util.*
enum class PostTypeEnum{
    Post, Award, Education, Message
}

@Entity(
        tableName = "posts",
        foreignKeys = [ForeignKey(entity = Person::class, parentColumns = ["id"], childColumns = ["person_id"],
                onDelete = ForeignKey.CASCADE)],
        indices = [Index("person_id")]
)
data class Post(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val postId: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "person_id") val personId: String,
        @ColumnInfo(name = "date") val postDate: Date,
        @ColumnInfo(name = "mediaUrl")val mediaUrl: String,
        @ColumnInfo(name = "type")val type: Int,
        @ColumnInfo(name = "liked")val liked: Boolean,
        @ColumnInfo(name = "txt_header")val postTxtHeader: String,
        @ColumnInfo(name = "txt")val postTxt: String
) {

   /* fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }
*/

    override fun toString() = postTxtHeader
}
