package com.hsd.avh.standstrong.utilities

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


object FirebaseUtils {

    fun loginAttempt(code:String):Bundle {
        val bundle = Bundle()
        bundle.putString("LOGIN_ATTEMPT_CODE", code)
        return bundle
    }

    fun addEvent(id : String, name: String): Bundle {

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        return bundle

    }

}