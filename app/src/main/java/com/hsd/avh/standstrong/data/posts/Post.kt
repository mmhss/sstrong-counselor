package com.hsd.avh.standstrong.data.posts

import androidx.room.*
import com.hsd.avh.standstrong.data.people.Person
import java.util.*
enum class PostTypeEnum{
    Post, Award, Education, Message
}
/*
foreignKeys = [ForeignKey(entity = Person::class, parentColumns = ["id"], childColumns = ["person_id"],
onDelete = ForeignKey.CASCADE)],*/
@Entity(
        tableName = "posts",

        indices = [Index(value = ["date", "person_id", "type"], unique = true)]
)
data class Post(

        @ColumnInfo(name = "person_id") val personId: String,
        @ColumnInfo(name = "mother_id") val motherId: Int,
        @ColumnInfo(name = "date") val postDate: Date,
        @ColumnInfo(name = "avatarUrl")val avatarUrl: String,
        @ColumnInfo(name = "card_header")val cardHeader: String,
        @ColumnInfo(name = "card_sub_header")val cardSubHeader: String,
        @ColumnInfo(name = "mediaUrl")val mediaUrl: String,
        @ColumnInfo(name = "liked")val liked: Boolean,
        @ColumnInfo(name = "comment_count")val commentCount: Int,
        @ColumnInfo(name = "type")val type: Int,
        @ColumnInfo(name = "post_header")val postHeader: String,
        @ColumnInfo(name = "post_sub_header")val postSubHeader: String,
        @ColumnInfo(name = "award_id") val awardId: String = "",
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val postId: Int = 0

) {

    fun printThis(): String {
        return "Post(personId='$personId', motherId=$motherId, postDate=$postDate, avatarUrl='$avatarUrl', cardHeader='$cardHeader', cardSubHeader='$cardSubHeader', mediaUrl='$mediaUrl', liked=$liked, commentCount=$commentCount, type=$type, postHeader='$postHeader', postSubHeader='$postSubHeader', postId=$postId)"
    }

    /* fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
         since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }
 */

    override fun toString() = postHeader
}
