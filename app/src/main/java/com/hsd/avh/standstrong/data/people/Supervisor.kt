package com.hsd.avh.standstrong.data.posts

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.hsd.avh.standstrong.data.people.Person
import java.util.*

/**
 * [Supervisor] enumerates which counsellor [Person]s are associated with which supervisors
 */


@Entity(
        tableName = "supervisors",
        foreignKeys = [ForeignKey(entity = Person::class, parentColumns = ["id"], childColumns = ["counsellor_id"],
                onDelete = CASCADE)],
        indices = [Index("supervisor_id","counsellor_id")]
)
data class Supervisor(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "counsellor_id") val counsellorId: String,
        @ColumnInfo(name = "supervisor_id") val supervisorId: String
) {

    override fun toString() = id
}
