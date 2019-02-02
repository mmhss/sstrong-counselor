package com.hsd.avh.standstrong.utilities

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event.LOGIN
import android.R.attr.name
import android.app.Application
import android.util.StatsLog.logEvent
import com.hsd.avh.standstrong.MainActivity


object FirebaseUtils {

    lateinit var firebaseAnalytics: FirebaseAnalytics

    fun loginAttempt(code:String):Bundle {
        val bundle = Bundle()
        bundle.putString("LOGIN_ATTEMPT_CODE", code)
        return bundle
    }

    fun addUserLoggedIn() {




    }
    fun addEvent() {


   /*     val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")*/

    }

    fun addUser() {
        /*val bundle = Bundle()
        bundle.putString(Param.USERNAME.name(), username)
        firebaseAnalytics.logEvent(EventType.LOGIN.name(), bundle)*/
    }
}