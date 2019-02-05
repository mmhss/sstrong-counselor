package com.hsd.avh.standstrong.utilities

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.crashlytics.android.Crashlytics
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hsd.avh.standstrong.LoginActivity
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.api.ApiEndpoints
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.posts.ApiActivity
import com.hsd.avh.standstrong.data.posts.ApiGPS
import com.hsd.avh.standstrong.data.posts.ApiProximity
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.workers.MyAPIException
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*




class SSUtils {

    companion object {
        const val SOCIAL_SUPPORT= "SocialSupport"
        const val SELF_CARE = "SelfCare"
        const val ROUTINE = "Routine"
        const val MOVEMENT = "Movement"
        const val BONUS = "Bonus"
        const val NEW_MESSAGE = -1

        var sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val database = AppDatabase.getInstance(StandStrong.applicationContext())
        var endpoints: ApiEndpoints? = ApiService.service


        @JvmStatic fun setNextEducationalPost(currentPost:String) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
            val editor = sharedPref.edit()
            val nextPost = "a" + Integer.toString(Integer.parseInt(currentPost.substring(1))+1)
            editor.putString("eduPost",nextPost)
            editor.apply()
        }

        @JvmStatic fun getNextEducationalPost():String {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
            return sharedPref.getString("eduPost","a1");
        }

        @JvmStatic fun getEducationalPostTitle(name: String): String {
            val findString = "post_title_edu_$name"
            return StandStrong.applicationContext().getString(StandStrong.applicationContext().resources.getIdentifier(findString, "string", StandStrong.applicationContext().packageName))
        }

        @JvmStatic fun getEducationalPostText(name: String): String {
            val findString = "post_subtitle_edu_$name"
            return StandStrong.applicationContext().getString(StandStrong.applicationContext().resources.getIdentifier(findString, "string", StandStrong.applicationContext().packageName))
        }

        @JvmStatic fun updateLiked(postId:Int, value: Boolean) {
            CoroutineScope(Dispatchers.IO).launch {
                database.postDao().updateLiked(postId,!value)
            }

        }

        @JvmStatic fun checkForNewPeople() {
            endpoints?.getMother()?.enqueue(object : Callback<List<ApiPerson>> {
                override fun onResponse(call: Call<List<ApiPerson>>, response: Response<List<ApiPerson>>) {
                    if (response.isSuccessful()) {
                        var isMoms : Boolean = false;
                        for (mother in response.body().orEmpty()) {
                            var imgUrl = "https://www.tinygraphs.com/squares/"+mother.identificationNumber.toString()+"?theme=heatwave&numcolors=4&size=50&fmt=png"
                            var p: Person = Person(mother.identificationNumber.toString(),mother.firstName.toString(),mother.lastName.toString(),  mother.id!!,1,imgUrl)
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

        @JvmStatic fun checkForNewPosts() {
            //Education Comes through a workManager

            checkForNewProximity()
            checkForNewGPS()
            checkForNewMessages()
            //checkForNewActivity()
        }


        @JvmStatic private fun createDataPost(motherIds : ArrayList<Int>, ct : Int,drawable:String, pstTitle : Int, pstTxt : Int) {
            if(motherIds.isNotEmpty()){
                triggerNotification(999,StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_description_post))
                var uniqueMomIds : Set<Int> = HashSet<Int>(motherIds)
                for (mom in uniqueMomIds) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var date = Date()
                            var cardTitle = StandStrong.applicationContext().getString(ct)
                            var mURL = drawable
                            var ssId = database.personDao().getMotherSsId(mom)
                            var aURL = "https://www.tinygraphs.com/squares/"+ssId+"?theme=heatwave&numcolors=4&size=50&fmt=png"
                            var postTitle = StandStrong.applicationContext().getString(pstTitle) + ssId
                            var postTxt =StandStrong.applicationContext().getString(pstTxt)
                            var p: Post = Post( ssId, mom,date,aURL,cardTitle,ssId,mURL,false,0,2,postTitle,postTxt)
                            database.postDao().insertPost(p)
                        } catch (e: Exception) {
                            Crashlytics.logException(e)
                        }
                    }
                }
            }
        }


        @JvmStatic fun checkForNewProximity() {
            endpoints?.getProximityData()?.enqueue(object : Callback<List<ApiProximity>> {
                override fun onResponse(call: Call<List<ApiProximity>>, response: Response<List<ApiProximity>>) {
                    if (response.isSuccessful) {
                        val motherIds = ArrayList<Int>()
                        for (r in response.body().orEmpty()) {
                            motherIds.add(r.motherId!!)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    database.proximityDao().insertProximity(r)
                                } catch (e: Exception) {
                                    Crashlytics.logException(e)
                                }
                            }
                        }
                        createDataPost(motherIds, R.string.card_title_proximity,"@drawable/data-routine",R.string.post_title_proximity,R.string.post_subtitle_proximity)
                    } else {
                        val statusCode = response.code()
                        // handle request errors depending on status code
                        var e = MyAPIException(response.message(),response.code())
                        Crashlytics.logException(e)
                    }
                }
                override fun onFailure(call: Call<List<ApiProximity>>, t: Throwable) {
                    var e = MyAPIException("Post Proximity Call Failed",95)
                    Crashlytics.logException(e)
                }
            })
        }

        @JvmStatic fun checkForNewGPS() {
            endpoints?.getGPSData()?.enqueue(object : Callback<List<ApiGPS>> {
                    override fun onResponse(call: Call<List<ApiGPS>>, response: Response<List<ApiGPS>>) {
                        if (response.isSuccessful) {
                            val motherIds = ArrayList<Int>()
                            for (r in response.body().orEmpty()) {
                                motherIds.add(r.motherId!!)
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        database.gpsDao().insertGPS(r)
                                    } catch (e: Exception) {
                                        Crashlytics.logException(e)
                                    }
                                }
                            }
                            createDataPost(motherIds, R.string.card_title_gps,"@drawable/data-gps",R.string.post_title_gps,R.string.post_subtitle_gps)
                        } else {
                            val statusCode = response.code()

                            var e = MyAPIException(response.message(),response.code())
                            Crashlytics.logException(e)
                        }
                    }
                    override fun onFailure(call: Call<List<ApiGPS>>, t: Throwable) {
                        var e = MyAPIException("Post GPS Call Failed",85)
                        Crashlytics.logException(e)
                    }
                })
        }

        @JvmStatic fun checkForNewActivity() {
            endpoints?.getActivityData()?.enqueue(object : Callback<List<ApiActivity>> {
                override fun onResponse(call: Call<List<ApiActivity>>, response: Response<List<ApiActivity>>) {
                    if (response.isSuccessful) {
                        val motherIds = ArrayList<Int>()
                        for (r in response.body().orEmpty()) {
                            motherIds.add(r.motherId!!)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    database.activityDao().insertActivity(r)
                                } catch (e: Exception) {
                                    Crashlytics.logException(e)
                                }
                            }
                        }
                        createDataPost(motherIds, R.string.card_title_activity,"@drawable/data-activity",R.string.post_title_activity,R.string.post_subtitle_activity)
                    } else {
                        val statusCode = response.code()
                        // handle request errors depending on status code
                        var e = MyAPIException(response.message(),response.code())
                        Crashlytics.logException(e)
                    }
                }
                override fun onFailure(call: Call<List<ApiActivity>>, t: Throwable) {
                    var e = MyAPIException("Post Activity Call Failed",90)
                    Crashlytics.logException(e)
                }
            })
        }

        @JvmStatic fun checkForNewAwards() {
            endpoints?.getAwards()?.enqueue(object : Callback<List<ApiAward>> {
                override fun onResponse(call: Call<List<ApiAward>>, response: Response<List<ApiAward>>) {
                    if (response.isSuccessful) {
                        var isAwards : Boolean = false;
                        for (award in response.body().orEmpty()) {
                            var date = Date()
                            var awardStr = "default"
                            var levelStr = "0"
                            try{
                                date = SimpleDateFormat("yyyy-MM-dd").parse(award.awardForDate)
                                awardStr = switchAward(award.awardType!!)
                                levelStr = Integer.toString(award.awardLevel!!)
                            } catch (e:java.lang.Exception) {
                                date = Date()
                            }
                            val fileNameString = "@drawable/" + awardStr + "_l"+levelStr
                            var a: Award = Award(award.mother!!.id!!,date,fileNameString)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    database.awardDao().insertAward(a)
                                    //Also make a post that their is a new award.
                                    var cardTitle = StandStrong.applicationContext().getString(R.string.card_title_award)
                                    var mURL = "@drawable/new-award"
                                    var aURL = "https://www.tinygraphs.com/squares/"+a.motherId+"?theme=heatwave&numcolors=4&size=50&fmt=png"
                                    var postTitle = StandStrong.applicationContext().getString(R.string.post_title_award)
                                    var postTxt =StandStrong.applicationContext().getString(R.string.post_subtitle_award)
                                    var ssId = database.personDao().getMotherSsId(a.motherId)
                                    var p: Post = Post( ssId,a.motherId,date,aURL,cardTitle,ssId,mURL,false,0,2,postTitle,postTxt)
                                    database.postDao().insertPost(p)
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

        @JvmStatic private fun switchAward(s : String) : String {
            return when (s) {
                SOCIAL_SUPPORT -> "ss"
                SELF_CARE -> "sc"
                ROUTINE -> "rt"
                MOVEMENT -> "mm"
                BONUS -> "bonus"
                else -> "na"
            }
        }

        @JvmStatic fun uploadMessage(id : Long) {

            var m : Message? = null
            var endpoints: ApiEndpoints? = ApiService.service
            runBlocking {
                m = async {
                    CoroutineScope(Dispatchers.IO).async {
                        database.messageDao().getMessageById(id)
                    }.await()
                }.await()
                if (m != null) {
                    endpoints?.postMessages(m!!)?.enqueue(object : Callback<Message> {
                        override fun onResponse(call: Call<Message>, response: Response<Message>) {
                            if (response.isSuccessful) {
                                Log.d("SSTRNG","Success")
                            } else {
                                val statusCode = response.code()
                                var e = MyAPIException(response.message(),response.code())
                                Crashlytics.logException(e)
                            }
                        }
                        override fun onFailure(call: Call<Message>, t: Throwable) {
                            var e = MyAPIException("Upload New Messages Failed",98)
                            Crashlytics.logException(e)
                        }
                    })
                }
            }

        }

        @JvmStatic fun checkForNewMessages(){
            var endpoints: ApiEndpoints? = ApiService.service
            endpoints?.retrieveMessages()?.enqueue(object : Callback<List<Message>> {
                override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                    if (response.isSuccessful) {
                        for (msg in response.body().orEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                var msgT = -1
                                if(msg.msgThread == NEW_MESSAGE) {
                                    //New so must make a post for this message thread
                                    var date = SimpleDateFormat("dd/MM/yyyy-MM-dd").parse("01/01/2019")
                                    var cardTitle = StandStrong.applicationContext().getString(R.string.card_title_msg)
                                    var mURL = "@drawable/new-message"
                                    var aURL = "https://www.tinygraphs.com/squares/"+msg.motherId+"?theme=heatwave&numcolors=4&size=50&fmt=png"
                                    var postTitle = StandStrong.applicationContext().getString(R.string.post_title_msg)
                                    var postTxt =StandStrong.applicationContext().getString(R.string.post_subtitle_msg)
                                    var ssId = database.personDao().getMotherSsId(msg.motherId)
                                    var p: Post = Post( ssId,msg.motherId,date,aURL,cardTitle,ssId,mURL,false,0,2,postTitle,postTxt)
                                    msgT = database.postDao().insertPost(p).toInt()
                                }
                                val m = Message(99123,
                                        "My Mesage",StandStrong.MESSAGE_DIRECTION_SEND,
                                        msgT ,
                                        Calendar.getInstance()
                                )
                                database.messageDao().insertMessage(m)
                            }
                        }
                    } else {
                        val statusCode = response.code()
                        var e = MyAPIException(response.message(),response.code())
                        Crashlytics.logException(e)
                    }
                }
                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    var e = MyAPIException("Upload New Messages Failed",98)
                    Crashlytics.logException(e)
                }
            })
        }


        @JvmStatic fun triggerNotification(DBEventID : Int, description : String) {
            //create an intent to open the event details activity
            var intent = Intent(StandStrong.applicationContext(), LoginActivity::class.java)
            val title = "StandStrong Post"
            intent.putExtra("isNotificationTap", true)
            intent.putExtra("title", title)
            intent.putExtra("pubTime", Calendar.getInstance().time.time)
            intent.putExtra("notification", true)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.action = "uniqueAction"
            var pendingIntent = PendingIntent.getActivity(StandStrong.applicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            //build the notification
            val notificationBuilder = NotificationCompat.Builder(StandStrong.applicationContext(), StandStrong.NOTIFICATION_CHANNEL)
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
            val notificationManager = NotificationManagerCompat.from(StandStrong.applicationContext())

            //we give each notification the ID of the event it's describing,
            //to ensure they all show up and there are no duplicates
            notificationManager.notify(DBEventID, notificationBuilder.build())

        }

    }

}
