package com.hsd.avh.standstrong.data

class ErrorModel {

    var timestamp: String? = null
    val status: Int? = null
    val error: String? = null
    val message: String? = null
    val path: String? = null

    override fun toString(): String {
        return "ErrorModel(timestamp=$timestamp, status=$status, error=$error, message=$message, path=$path)"
    }
}