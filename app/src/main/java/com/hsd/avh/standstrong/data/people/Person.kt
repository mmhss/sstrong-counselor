package com.hsd.avh.standstrong.data.people

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

/**
 * [Person] represents a Standstrong User. They can be counsellors, supervisors, mothers and their
 * family memebers. All [Award]s are linked to a person, as are all [Posts]
 *
 * Declaring the column info allows for the renaming of variables without implementing a
 * database migration, as the column name would not change.
 */

enum class PersonTypeEnum{
    Client, Counsellor, Supervisor
}
@Entity(tableName = "people",
        indices = arrayOf(Index(value = ["ss_id"],
        unique = true)))
data class Person(
        @ColumnInfo(name = "ss_id") val ssId: String,
        @ColumnInfo(name = "first_name") val firstName: String,
        @ColumnInfo(name = "last_name") val lastName: String,
        @ColumnInfo(name = "mother_id") val mother_id: Int,
        @ColumnInfo(name = "type") val type: Int,
        @ColumnInfo(name = "img_url") val imgUrl: String,
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val personId: String = UUID.randomUUID().toString()
) {


    override fun toString() = personId

        fun printMe(): String { return "Person(ssId='$ssId', firstName='$firstName', lastName='$lastName', mother_id=$mother_id, type=$type, imgUrl='$imgUrl', personId='$personId')" }

}

