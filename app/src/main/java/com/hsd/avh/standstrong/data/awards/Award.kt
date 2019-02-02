package com.hsd.avh.standstrong.data.awards

import androidx.room.*
import com.hsd.avh.standstrong.data.people.Person
import java.util.*

enum class AwardTypeEnum{
        HAPPY_BABY,SELF_CARE,SOCIAL_SUPPORT,SUNLIGHT,MOVEMENT,BONUS
}

/**
 * [Award] represents when a [Person] achieves one of the goals set through the passive data monitoring
 * They are available online through the rest call
 *
 * Declaring the column info allows for the renaming of variables without implementing a
 * database migration, as the column name would not change.
 */
@Entity(
    tableName = "awards",
    foreignKeys = [ForeignKey(entity = Person::class, parentColumns = ["first_name"], childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE)],
    indices = arrayOf(Index(value = ["person_id","filename"],
            unique = true))
)


data class Award(
    @ColumnInfo(name = "person_id") val personId: String,
    @ColumnInfo(name = "date") val awardDate: Date,
    @ColumnInfo(name = "filename")val fileName: String,
    @ColumnInfo(name = "title")val title: String,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var awardId: String = UUID.randomUUID().toString()
) {

}
