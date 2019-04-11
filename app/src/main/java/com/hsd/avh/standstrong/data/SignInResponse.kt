package com.hsd.avh.standstrong.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignInResponse {

    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null

    override fun toString(): String {
        return "SignInResponse(token=$token, type=$type)"
    }

}