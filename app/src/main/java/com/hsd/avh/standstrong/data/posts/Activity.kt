package com.hsd.avh.standstrong.data.posts


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "activity",
        indices = arrayOf(Index(value = ["id"],
                unique = true)))
data class Activity (
        @ColumnInfo(name = "activityType")  var activityType: String? = null,
        @ColumnInfo(name = "confidence") var confidence: Int? = null,
        @ColumnInfo(name = "mother_id") var motherId: Int? = null,
        @ColumnInfo(name = "capture_date") var captureDate: Date? = null,
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false)  var id: Int? = null
){}