package com.hsd.avh.ema.emaScheduler

import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.crashlytics.android.Crashlytics
import com.hsd.avh.standstrong.LoginActivity
import java.util.*
import java.util.concurrent.TimeUnit
import com.hsd.avh.standstrong.MainActivity
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.api.ApiEndpoints
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.http2.ErrorCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScheduleNotificationWorker(context : Context, params : WorkerParameters)
    : Worker(context, params) {


    companion object {
        private const val TAG = "SSNotification"

    }
    val database = AppDatabase.getInstance(applicationContext)

    override fun doWork(): Result {

        queryAPIForNewData()
        setupNextScheduledNotification()
        return Result.success()

    }

    private fun queryAPIForNewData(){
        var endpoints: ApiEndpoints? = ApiService.service
        getPeople(endpoints)
        getAwards(endpoints)
        //getPosts(endpoints)
    }

    private fun getPeople(endpoints: ApiEndpoints?) {
        endpoints?.getMother()?.enqueue(object : Callback<List<ApiPerson>> {
            override fun onResponse(call: Call<List<ApiPerson>>, response: Response<List<ApiPerson>>) {
                if (response.isSuccessful()) {
                    var isMoms : Boolean = false;
                    for (mother in response.body().orEmpty()) {
                        var p:Person = Person(mother.identificationNumber.toString(),"",1,"https://robohash.org/nonquibusdamipsam.png?size=50x50&set=set1")
                        CoroutineScope(Dispatchers.IO).launch {
                            database.personDao().insertPerson(p)
                        }
                        isMoms = true;
                    }
                    if(isMoms)
                        triggerNotification(999,StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_description_people))
                } else {
                    val statusCode = response.code()
                    // handle request errors depending on status code
                    var e = MyAPIException(response.message(),response.code())
                    Crashlytics.logException(e)
                }
            }
            override fun onFailure(call: Call<List<ApiPerson>>, t: Throwable) {
                var e = MyAPIException("Person Call Failed",99)
                Crashlytics.logException(e)
            }
        })
    }

    private fun getAwards(endpoints: ApiEndpoints?) {
        endpoints?.getAwards()?.enqueue(object : Callback<List<ApiAward>> {
            override fun onResponse(call: Call<List<ApiAward>>, response: Response<List<ApiAward>>) {
                if (response.isSuccessful) {
                    var isAwards : Boolean = false;
                    for (award in response.body().orEmpty()) {
                       var date = Date()
                        var a: Award = Award("SS1001",date,"@drawable/sc_l1_n","सामाजिक समर्थन - स्तर 1")

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                            database.awardDao().insertAward(a)
                            } catch (e: Exception) {
                                Crashlytics.logException(e)
                            }
                        }
                        isAwards = true;
                    }
                    if(isAwards)
                        triggerNotification(999,StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_description_award))
                } else {
                    val statusCode = response.code()
                    // handle request errors depending on status code
                    var e = MyAPIException(response.message(),response.code())
                    Crashlytics.logException(e)
                }
            }

            override fun onFailure(call: Call<List<ApiAward>>, t: Throwable) {
                // handle request errors depending on status code
                var e = MyAPIException("Award Call Failed",99)
                Crashlytics.logException(e)
            }
        })
    }

    private fun setupNextScheduledNotification(){
        val notificationWork = OneTimeWorkRequest.Builder(ScheduleNotificationWorker::class.java)
                .setInitialDelay(24,TimeUnit.HOURS)
                .addTag(StandStrong.TAG)
                .build()
        WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG, ExistingWorkPolicy.REPLACE ,notificationWork);
    }

    fun triggerNotification(DBEventID : Int, description : String) {
        //create an intent to open the event details activity
        var intent = Intent(applicationContext, LoginActivity::class.java)
        val title = "StandStrong Post"
        intent.putExtra("isNotificationTap", true)
        intent.putExtra("title", title)
        intent.putExtra("pubTime", Calendar.getInstance().time.time)
        intent.putExtra("notification", true)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.action = "uniqueAction"
        var pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //build the notification
        val notificationBuilder = NotificationCompat.Builder(applicationContext, StandStrong.NOTIFICATION_CHANNEL)
                .setSmallIcon(com.hsd.avh.standstrong.R.mipmap.ic_stat_ganesha)
                .setTicker(StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_ticker))
                .setContentTitle(StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_title))
                .setContentText(description)
                .setContentInfo("SSTRONG")
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
        //trigger the notification
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        //we give each notification the ID of the event it's describing,
        //to ensure they all show up and there are no duplicates
        notificationManager.notify(DBEventID, notificationBuilder.build())

    }


    inner class MyAPIException : Exception {

        val code: Int
        val serialVersionUID = 7718828512143293558L

        constructor(code: Int) : super() {
            this.code = code
        }

        constructor(message: String, cause: Throwable, code: Int) : super(message, cause) {
            this.code = code
        }

        constructor(message: String, code: Int) : super(message) {
            this.code = code
        }

        constructor(cause: Throwable, code: Int) : super(cause) {
            this.code = code
        }


    }
}