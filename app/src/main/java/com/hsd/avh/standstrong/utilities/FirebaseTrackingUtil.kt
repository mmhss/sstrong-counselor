package com.hsd.avh.standstrong.utilities

    import android.app.Activity
    import android.content.Context
    import com.google.firebase.analytics.FirebaseAnalytics

    class FirebaseTrackingUtil(val context: Context) {
        enum class Screens {
            Post,
            PostDetails,
            Awards,
            AwardDetails,
            Clients,
            ClientDetail
        }

        fun track(screen: Screens) {

                FirebaseAnalytics.getInstance(context.applicationContext)
                        .setCurrentScreen(context as Activity, screen.name, null)

            }
        }
