package com.hsd.avh.standstrong.data.posts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "gps",
        indices = arrayOf(Index(value = ["id"],
                unique = true)))
data class Gps (

    @ColumnInfo(name = "latitude") var latitude: Double? = null,
    @ColumnInfo(name = "longitude") var longitude: Double? = null,
    @ColumnInfo(name = "altitude") var altitude: Double? = null,
    @ColumnInfo(name = "accuracy")  var accuracy: Double? = null,
    @ColumnInfo(name = "mother_id") var motherId: Int? = null,
    @ColumnInfo(name = "record_date") var recordDate: Date? = null,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false)  var id: Int? = null
){}