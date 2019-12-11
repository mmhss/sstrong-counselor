package com.hsd.avh.standstrong.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginBody(@SerializedName("username")
                @Expose var userName: String?, @SerializedName("password")
                @Expose var password: String?) {

}