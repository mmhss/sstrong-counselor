package com.hsd.avh.standstrong

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import androidx.collection.ArrayMap
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.utilities.UserAuthentication
import com.hsd.avh.standstrong.utilities.UserTypes
import com.hsd.avh.standstrong.workers.ScheduleEduPostWorker
import com.hsd.avh.standstrong.workers.ScheduleNotificationWorker
import java.util.concurrent.TimeUnit


class StandStrong : Application() {


        init {
            instance = this
        }

        companion object {
            const val NOTIFICATION_CHANNEL = "stand strong"
            const val MESSAGE_DIRECTION_IN = "ToCounseler"
            const val MESSAGE_DIRECTION_OUT = "ToMother"
            const val MESSAGE_DIRECTION_OUT_ALL = "ToMothers"
            const val MESSAGE_ROW_ID= "DBMessageId"
            const val POST_CARD_MESSAGE = 1
            const val POST_CARD_PROXIMITY = 2
            const val POST_CARD_GPS = 3
            const val POST_CARD_ACTIVITY = 4
            const val POST_CARD_AWARD = 5
            const val POST_CARD_CONTENT = 6
            const val POST_CARD_GOAL = 7
            const val POST_CARD_STRING_MESSAGE = "Messages"
            const val POST_CARD_STRING_PROXIMITY = "Proximity"
            const val POST_CARD_STRING_GPS = "Movement"
            const val POST_CARD_STRING_ACTIVITY = "Activity"
            const val POST_CARD_STRING_GOAL = "Goals"
            const val POST_CARD_STRING_AWARD = "Awards"
            const val POST_CARD_STRING_CONTENT = "Information"
            const val NUMBER_OF_EDU_POSTS = 4
            const val ACTIVITY_CONFIDENCE= 50
            const val GPS_ACCURACY = 200
            const val EDU_POST_CHECK_FREQUENCY_DAYS = 15
            const val NEW_DATA_CHECK_FREQUENCY_DAYS = 1
            const val ACTIVITY_RUNNING = "Running"
            const val ACTIVITY_TILTING = "Tilting"
            const val ACTIVITY_STILL = "Still"
            const val ACTIVITY_FOOT = "On Foot"
            const val ACTIVITY_UNKNOWN = "Unknown"
            const val ACTIVITY_VEHICLE = "In Vehicle"
            const val ACTIVITY_BICYCLE = "On Bicycle"
            const val TAG_SCHEDULE = "SSTNG_SCHEDULE"
            const val TAG_EDU = "SSTNG_EDUCATIONAL"
            const val TAG_MSG = "SSTNG_MESSAGE"

            private val filter_list = ArrayMap<String, List<String>>()
            private val filter_people_list = ArrayMap<String, List<String>>()
            private var instance: StandStrong? = null

            fun firebaseInstance():FirebaseAnalytics{
                return FirebaseAnalytics.getInstance(applicationContext())
            }

            fun applicationContext() : Context {
                return instance!!.applicationContext
            }

            fun settEduPostsShown(count:Int) {
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this.applicationContext())
                val editor = sharedPref.edit()
                editor.putInt("eduCount",count)
                editor.apply()
            }

            fun getEduPostsShown() : Int{
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this.applicationContext())
                return sharedPref.getInt("eduCount",1);
            }

            fun startCollection() {
                this.instance!!.setupNotificationChannel()
                //Set up a daily checker for new data
                val notificationWork = OneTimeWorkRequest.Builder(ScheduleNotificationWorker::class.java)
                        .setInitialDelay(StandStrong.NEW_DATA_CHECK_FREQUENCY_DAYS.toLong(), TimeUnit.DAYS)
                        .addTag(StandStrong.TAG_SCHEDULE )
                        .build()
                WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG_SCHEDULE, ExistingWorkPolicy.REPLACE ,notificationWork)

                //Set up new edu post schedule
                val eduWork = OneTimeWorkRequest.Builder(ScheduleEduPostWorker::class.java)
                        .setInitialDelay(StandStrong.EDU_POST_CHECK_FREQUENCY_DAYS.toLong(), TimeUnit.SECONDS)
                        .addTag(StandStrong.TAG_EDU )
                        .build()
                WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG_EDU, ExistingWorkPolicy.REPLACE ,eduWork)
            }


            fun setUser(user:UserAuthentication) {
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
                val editor = sharedPref.edit()
                editor.putString("user",user.code)
                editor.apply()
            }



            fun isNotRA(): Boolean {
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
                val us = sharedPref!!.getString("user","1111").toCharArray()
                var user: UserAuthentication = UserAuthentication(us[0].toString(),us[1].toString(),us[2].toString(),us[3].toString())
                return user.userType() != UserTypes.C5555
            }

            fun getFilters(): ArrayMap<String,List<String>> {
                return filter_list
            }
            fun getFiltersPeople(): ArrayMap<String,List<String>> {
                return filter_people_list
            }
        }

        override fun onCreate() {
            super.onCreate()
            // initialize other global things
            StandStrong.applicationContext()
            //Begin the worker service to look for updates
            startCollection()
        }

        private fun setupNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //define the importance level of the notification
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            //build the actual notification channel, giving it a unique ID and name
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = "SStrong Notification Channel"
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }



}