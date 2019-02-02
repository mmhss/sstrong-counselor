package com.hsd.avh.standstrong.data.people


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiPerson {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("identificationNumber")
    @Expose
    var identificationNumber: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param id
     * @param status
     * @param identificationNumber
     */
    constructor(id: Int?, identificationNumber: String, status: String) : super() {
        this.id = id
        this.identificationNumber = identificationNumber
        this.status = status
    }

    fun withId(id: Int?): ApiPerson {
        this.id = id
        return this
    }

    fun withIdentificationNumber(identificationNumber: String): ApiPerson {
        this.identificationNumber = identificationNumber
        return this
    }

    fun withStatus(status: String): ApiPerson {
        this.status = status
        return this
    }

}