package com.hsd.avh.standstrong.data.posts

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hsd.avh.standstrong.data.awards.ApiAward.Mother
import java.text.SimpleDateFormat
import java.util.*


class ApiProximity {
    @SerializedName("chartEvent")
    @Expose
    var chartEvent: String? = null
    @SerializedName("chartValue")
    @Expose
    var chartValue: String? = null
    @SerializedName("chartDay")
    @Expose
    var chartDay: String? = null
    @SerializedName("chartHour")
    @Expose
    var chartHour: String? = null
    @SerializedName("mother")
    @Expose
    var mother: Mother? = null

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