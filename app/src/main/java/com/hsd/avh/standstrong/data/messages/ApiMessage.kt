package com.hsd.avh.standstrong.data.messages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiMessage {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("postedDate")
    @Expose
    var postedDate: String? = null
    @SerializedName("threadId")
    @Expose
    var threadId: Int? = null
    @SerializedName("direction")
    @Expose
    var direction: String? = null
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
     * @param message
     * @param id
     * @param postedDate
     * @param mother
     * @param threadId
     * @param direction
     */
    constructor(id: Int?, message: String, postedDate: String, threadId: Int?, direction: String, mother: Mother) : super() {
        this.id = id
        this.message = message
        this.postedDate = postedDate
        this.threadId = threadId
        this.direction = direction
        this.mother = mother
    }

    class Mother {

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