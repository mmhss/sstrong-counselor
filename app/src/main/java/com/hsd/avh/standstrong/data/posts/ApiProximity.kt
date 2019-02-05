package com.hsd.avh.standstrong.data.posts

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hsd.avh.standstrong.data.awards.ApiAward.Mother
import java.text.SimpleDateFormat
import java.util.*

@Entity(
        tableName = "proximity",
        indices = arrayOf(Index(value = ["mother_id","chart_day","chart_hour","chart_event"],
                unique = true)))
class ApiProximity {

    @SerializedName("chartEvent")
    @Expose
    @ColumnInfo(name = "chart_event")
    var chartEvent: String? = null
    @SerializedName("chartValue")
    @Expose
    @ColumnInfo(name = "chart_value")
    var chartValue: String? = null
    @SerializedName("chartDay")
    @Expose
    @ColumnInfo(name = "chart_day")
    var chartDay: String? = null
    @SerializedName("chartHour")
    @Expose
    @ColumnInfo(name = "chart_hour")
    var chartHour: String? = null
    @SerializedName("mother")
    @Expose
    @Ignore
    var mother: Mother? = null

    @ColumnInfo(name = "mother_id")
    var motherId: Int? = mother!!.id


    @ColumnInfo(name = "chart_date")
    var chartDate: Date? = null;

    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}


    /**
     *
     * @param chartHour
     * @param mother
     * @param chartValue
     * @param chartEvent
     * @param chartDay
     */
    constructor(chartEvent: String, chartValue: String, chartDay: String, chartHour: String, mother: Mother) : super() {
        this.chartEvent = chartEvent
        this.chartValue = chartValue
        this.chartDay = chartDay
        this.chartHour = chartHour
        this.mother = mother
        /*"chartDay": "2018-11-08",
        "chartHour": "18",*/
        var dt = this.chartDay +  " " + this.chartHour + ":00"
        this.chartDate =   SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dt)
    }

    inner class Mother {

        @SerializedName("id")
        @Expose
        @Ignore
        var id: Int? = null

        /**
         * No args constructor for use in serialization
         *
         */
        constructor() {}

        /**
         *
         * @param id
         */
        constructor(id: Int?) : super() {
            this.id = id
        }

    }

}