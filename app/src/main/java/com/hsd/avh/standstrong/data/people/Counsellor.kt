package com.hsd.avh.standstrong.data.posts

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.hsd.avh.standstrong.data.people.Person
import java.util.*

/**
 * [Counsellor] enumerates which client [Person]s are associated with which counsellors
 */


@Entity(
        tableName = "counsellors",
        foreignKeys = [ForeignKey(entity = Person::class, parentColumns = ["id"], childColumns = ["person_id"],
                onDelete = CASCADE)],
        indices = [Index("person_id","counsellor_id")]
)
data class Counsellor(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "person_id") val personId: String,
        @ColumnInfo(name = "counsellor_id") val counsellorId: String
) {

    override fun toString() = id
}
