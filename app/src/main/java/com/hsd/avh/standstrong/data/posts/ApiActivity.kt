package com.hsd.avh.standstrong.data.posts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiActivity {
    @SerializedName("id")
    @Expose
    var id:Int? = null
    @SerializedName("captureDate")
    @Expose
    var captureDate:String? = null
    @SerializedName("activityType")
    @Expose
    var activityType:String? = null
    @SerializedName("confidence")
    @Expose
    var confidence:Int? = null
    @SerializedName("mother")
    @Expose
    var mother:Mother? = null
    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}
    /**
     *
     * @param id
     * @param mother
     * @param confidence
     * @param activityType
     * @param captureDate
     */
    constructor(id:Int, captureDate:String, activityType:String, confidence:Int, mother:Mother) : super() {
        this.id = id
        this.captureDate = captureDate
        this.activityType = activityType
        this.confidence = confidence
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