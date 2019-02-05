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
    indices = arrayOf(Index(value = ["mother_id","filename"],
            unique = true))
)


data class Award(
    @ColumnInfo(name = "mother_id") val motherId: Int,
    @ColumnInfo(name = "date") val awardDate: Date,
    @ColumnInfo(name = "filename")val fileName: String,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var awardId: String = UUID.randomUUID().toString()
) {

}
