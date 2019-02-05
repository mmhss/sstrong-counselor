package com.hsd.avh.standstrong.data.awards

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiAward {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("awardType")
    @Expose
    var awardType: String? = null
    @SerializedName("awardLevel")
    @Expose
    var awardLevel: Int? = null
    @SerializedName("awardForDate")
    @Expose
    var awardForDate: String? = null
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
     * @param awardLevel
     * @param awardType
     * @param mother
     * @param awardForDate
     */
    constructor(id: Int?, awardType: String, awardLevel: Int?, awardForDate: String, mother: Mother) : super() {
        this.id = id
        this.awardType = awardType
        this.awardLevel = awardLevel
        this.awardForDate = awardForDate
        this.mother= mother
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