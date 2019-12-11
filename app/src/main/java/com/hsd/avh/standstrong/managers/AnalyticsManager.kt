package com.hsd.avh.standstrong.managers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsManager constructor(val context: Application) {

    enum class Screens {
        Post,
        PostDetails,
        ActivityData,
        GpsData,
        ProximityData,
        Awards,
        AwardDetails,
        Clients,
        ClientDetail,
        Messages,
        Filter
    }

    fun trackScreen(name: String, activity: Activity) {

        FirebaseAnalytics.getInstance(context).setCurrentScreen(activity, name, null)
    }

    fun trackEvent(s: String, bundle: Bundle? = null) {

        FirebaseAnalytics.getInstance(context).logEvent(s.toLowerCase().replace(" ", "_"), bundle)
    }

    fun setUserId(userGroupCode: String?) {

        FirebaseAnalytics.getInstance(context).setUserId(userGroupCode)
    }

    fun setUserProperty(s: String, userGroupName: String?) {

        FirebaseAnalytics.getInstance(context).setUserProperty(s, userGroupName)
    }
}