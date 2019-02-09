package com.hsd.avh.standstrong.data.posts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiProximity {

    @SerializedName("motherId")
    @Expose
    var motherId: Int? = null
    @SerializedName("chartDay")
    @Expose
    var chartDay: String? = null
    @SerializedName("chartHour")
    @Expose
    var chartHour: String? = null
    @SerializedName("chartEvent")
    @Expose
    var chartEvent: String? = null
    @SerializedName("chartValue")
    @Expose
    var chartValue: String? = null
    @SerializedName("proximityId")
    @Expose
    var proximityId: Int? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param chartHour
     * @param chartValue
     * @param chartEvent
     * @param proximityId
     * @param motherId
     * @param chartDay
     */
    constructor(motherId: Int?, chartDay: String, chartHour: String, chartEvent: String, chartValue: String, proximityId: Int?) : super() {
        this.motherId = motherId
        this.chartDay = chartDay
        this.chartHour = chartHour
        this.chartEvent = chartEvent
        this.chartValue = chartValue
        this.proximityId = proximityId
    }

}