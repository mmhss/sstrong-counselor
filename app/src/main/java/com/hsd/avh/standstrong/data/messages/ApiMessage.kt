package com.hsd.avh.standstrong.data.messages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hsd.avh.standstrong.utilities.Const
import java.text.SimpleDateFormat

class ApiMessage {

    @SerializedName("id")
    var id: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("postedDate")
    @Expose
    var postedDate: String? = null
    @SerializedName("threadId")
    @Expose
    var threadId: Long? = null
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
    constructor(id: Int?, message: String, postedDate: String, threadId: Long?, direction: String, mother: Mother) : super() {
        this.id = id
        this.message = message
        this.postedDate = postedDate
        this.threadId = threadId
        this.direction = direction
        this.mother = mother
    }

    constructor(message: String, postedDate: String, threadId: Long?, direction: String, mother: Mother) : super() {
        this.message = message
        this.postedDate = postedDate
        this.threadId = threadId
        this.direction = direction
        this.mother = mother
    }

    constructor(dbMessage: Message) {
        this.message = dbMessage.msg
        this.postedDate = SimpleDateFormat(Const.MESSAGE_DATE_FORMAT).format(dbMessage.msgDate)
        this.threadId = dbMessage.msgThread
        this.direction = dbMessage.direction
        this.mother = Mother(dbMessage.motherId)
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

    override fun toString(): String {
        return "ApiMessage(id=$id, message=$message, postedDate=$postedDate, threadId=$threadId, direction=$direction, mother=$mother)"
    }
}