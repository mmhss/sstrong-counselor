package com.hsd.avh.standstrong

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import com.hsd.avh.ema.emaScheduler.ScheduleNotificationWorker
import java.util.concurrent.TimeUnit


class StandStrong : Application() {


        init {
            instance = this
        }

        companion object {
            const val NOTIFICATION_CHANNEL = "stand strong"
            const val TAG = "SSTNG"
            private var instance: StandStrong? = null

            fun firebaseInstance():FirebaseAnalytics{
                return FirebaseAnalytics.getInstance(applicationContext())
            }

            fun applicationContext() : Context {
                return instance!!.applicationContext
            }

            fun startCollection() {
                this.instance!!.setupNotificationChannel()
                //Just get a test off after 10 seconds then use real scheduling going forward
                val notificationWork = OneTimeWorkRequest.Builder(ScheduleNotificationWorker::class.java)
                        .setInitialDelay(3, TimeUnit.SECONDS)
                        .addTag(StandStrong.TAG )
                        .build()
                WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG, ExistingWorkPolicy.REPLACE ,notificationWork);
            }

        }

        override fun onCreate() {
            super.onCreate()
            // initialize other global things
            val context: Context = StandStrong.applicationContext()
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