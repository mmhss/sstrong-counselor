package com.hsd.avh.standstrong.data.posts


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "proximity",
        indices = arrayOf(Index(value = ["id"],
                unique = true)))
data class Proximity (
        @ColumnInfo(name = "chart_event") var chartEvent: String? = null,
        @ColumnInfo(name = "chart_value") var chartValue: String? = null,
        @ColumnInfo(name = "mother_id") var motherId: Int? = null,
        @ColumnInfo(name = "chart_date") var chartDate: Date? = null,
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: Int? = null

        /*"chartDay": "2018-11-08",
            "chartHour": "18",*/
        //var dt = this.chartDay +  " " + this.chartHour + ":00"
        //this.chartDate =   SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dt)
){}