package com.hsd.avh.standstrong.data.posts


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ApiGPS {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("captureDate")
    @Expose
    var captureDate: String? = null
    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null
    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null
    @SerializedName("altitude")
    @Expose
    var altitude: Double? = null
    @SerializedName("accuracy")
    @Expose
    var accuracy: Double? = null
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
     * @param id
     * @param mother
     * @param altitude
     * @param longitude
     * @param latitude
     * @param captureDate
     * @param accuracy
     */
    constructor(id: Int?, captureDate: String, latitude: Double?, longitude: Double?, altitude: Double?, accuracy: Double?, mother: Mother) : super() {
        this.id = id
        this.captureDate = captureDate
        this.latitude = latitude
        this.longitude = longitude
        this.altitude = altitude
        this.accuracy = accuracy
        this.mother = mother
    }

    inner class Mother {

        @SerializedName("id")
        @Expose
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