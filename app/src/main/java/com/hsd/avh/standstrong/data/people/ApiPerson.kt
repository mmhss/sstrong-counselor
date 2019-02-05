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
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null
    @SerializedName("middleName")
    @Expose
    var middleName: String? = null
    @SerializedName("lastName")
    @Expose
    var lastName: String? = null
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
     * @param middleName
     * @param id
     * @param lastName
     * @param status
     * @param firstName
     * @param identificationNumber
     */
    constructor(id: Int?, identificationNumber: String, firstName: String, middleName: String, lastName: String, status: String) : super() {
        this.id = id
        this.identificationNumber = identificationNumber
        this.firstName = firstName
        this.middleName = middleName
        this.lastName = lastName
        this.status = status
    }
}