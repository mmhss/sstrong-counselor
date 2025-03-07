package com.hsd.avh.standstrong.utilities

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.hsd.avh.standstrong.LoginActivity
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.api.ApiEndpoints
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.data.ErrorModel
import com.hsd.avh.standstrong.data.LoginBody
import com.hsd.avh.standstrong.data.SignInResponse
import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.messages.ApiMessage
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.posts.*
import com.hsd.avh.standstrong.workers.MyAPIException
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class SSUtils {

    companion object {
        private val ACTIVITY_LIMIT: Int = 1000
        private val ITEMS_LIMIT: Int = 200
        const val TAG = "SSUtils"
        const val SOCIAL_SUPPORT= "SocialSupport"
        const val SELF_CARE = "SelfCare"
        const val ROUTINE = "Routine"
        const val MOVEMENT = "Movement"
        const val BONUS = "Bonus"
        const val NEW_MESSAGE = -1
        const val DATA_GPS = "@drawable/data_gps"
        const val DATA_ACTIVITY = "@drawable/data_activity"
        const val DATA_ROUTINE = "@drawable/data_routine"
        const val NEW_MESSAGE_DRAWABLE = "@drawable/new_message"
        const val GOAL_DRAWABLE = "@drawable/data_goal"
        const val NEW_AWARD = "@drawable/new_award"
        //Tables
        const val PROXIMITY = "proximity"
        const val ACTIVITY = "activity"
        const val POSTS = "posts"
        const val PEOPLE = "people"
        const val GPS = "gps"
        const val AWARDS = "awards"
        const val MESSAGES = "messages"

        var sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val database = AppDatabase.getInstance(StandStrong.applicationContext())
        var endpoints: ApiEndpoints? = ApiService.service
        lateinit var returnSsId : String

        @JvmStatic fun setNextEducationalPost() {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
            val editor = sharedPref.edit()
            val currentPost:Int = StandStrong.getEduPostsShown()
            val nextPost = "post_" + Integer.toString(currentPost+1)
            editor.putString("eduPost",nextPost)
            editor.apply()
            StandStrong.settEduPostsShown(currentPost+1)
        }

        @JvmStatic fun login(userName: String, password: String, callback: Callback<SignInResponse> = object : Callback<SignInResponse> {
            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                Log.e(TAG, "error while login " + Log.getStackTraceString(t))
            }

            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {

                Log.d(TAG, "success relogin " + response.body()?.token)
                PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext()).edit().putString(Const.ARG_TOKEN, response.body()!!.token).apply()
            }
        }) {

            doAsync {

                endpoints?.login(LoginBody(userName, password))?.enqueue(callback)
            }
        }

        @JvmStatic fun getNextEducationalPost():String {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
            return sharedPref!!.getString("eduPost","post_1")
        }

        @JvmStatic fun getEducationalPostTitle(name: String): String {
            val findString : String  = "post_title_edu_$name"
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

        @JvmStatic fun loadPeople() {

            doAsync {

                endpoints!!.getPeopleAsync(getLastRow(PEOPLE)).enqueue(object : Callback<List<ApiPerson>> {
                    override fun onFailure(call: Call<List<ApiPerson>>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<List<ApiPerson>>, response: Response<List<ApiPerson>>) {

                        val errorString = response.errorBody()?.string()

                        if (errorString.isNullOrEmpty()) {

                            Log.d(TAG, "error str $errorString")

                            val persons = response.body()

                            if (persons != null) {

                                doAsync {
                                    for (mother in persons) {

                                        var imgUrl =
                                            "https://www.tinygraphs.com/squares/" + mother.identificationNumber.toString() + "?theme=heatwave&numcolors=4&size=50&fmt=png"
                                        var p: Person = Person(
                                            mother.identificationNumber.toString(),
                                            mother.firstName.toString(),
                                            mother.lastName.toString(),
                                            mother.id!!,
                                            1,
                                            imgUrl
                                        )
                                        database.personDao().insertPerson(p)
                                        updateLastRetrievedRow(PEOPLE, mother.id!!)
                                    }

                                    checkForNewPosts()
                                }
                            }
                        }
                    }
                })
            }

            GlobalScope.launch(Dispatchers.Main) {
                val request = endpoints!!.getMotherAsync(getLastRow(PEOPLE))
                try {
                    val response = request.await()
                    for (mother in response.body().orEmpty()) {

                    }
                    if(response.body().orEmpty().isNotEmpty())
                        triggerNotification(999,StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_description_people))
                } catch (e: HttpException) {
                    Crashlytics.logException(e)
                } catch (e: Throwable) {
                    Crashlytics.logException(e)
                } catch (e: Exception) {
                    Crashlytics.logException(e)
                }
            }
        }

        @JvmStatic fun checkForNewPeople() {
            GlobalScope.launch(Dispatchers.Main) {
                val request = endpoints!!.getMotherAsync(getLastRow(PEOPLE))
                try {
                    val response = request.await()
                    for (mother in response.body().orEmpty()) {
                        var imgUrl = "https://www.tinygraphs.com/squares/"+mother.identificationNumber.toString()+"?theme=heatwave&numcolors=4&size=50&fmt=png"
                        var p: Person = Person(mother.identificationNumber.toString(),mother.firstName.toString(),mother.lastName.toString(),  mother.id!!,1,imgUrl)
                        withContext(Dispatchers.IO) {
                            try {
                                database.personDao().insertPerson(p)
                            } catch(e:Exception) {
                                Log.d("SSS","Error")
                            }
                        }
                        updateLastRetrievedRow(PEOPLE, mother.id!!)
                    }
                    if(response.body().orEmpty().isNotEmpty())
                        triggerNotification(999,StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_description_people))
                } catch (e: HttpException) {
                    Crashlytics.logException(e)
                } catch (e: Throwable) {
                    Crashlytics.logException(e)
                } catch (e: Exception) {
                    Crashlytics.logException(e)
                }
            }
        }

       /* @JvmStatic private  fun countPersons():Int {
            var count = 0;
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    count = database.personDao().countPeople()
                }
            }
            return count;
        }
*/
        @JvmStatic private  fun countAwards():Int {
            var count = 0;
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    count = database.awardDao().countAwards()
                }
            }
            return count;
        }

        @JvmStatic fun checkForNewPosts() {
            //Education Comes through a workManager
            checkForNewProximity()
            checkForNewGPS()
            checkForNewMessages()
            checkForNewActivity()
        }

        @JvmStatic private fun createDataPost(motherIds:ArrayList<String>,ct : Int,drawable:String, pstTitle : Int, pstTxt : Int, type:Int) {
            if (motherIds.isNotEmpty()) {
                triggerNotification(999, StandStrong.applicationContext().resources.getString(com.hsd.avh.standstrong.R.string.notification_description_post))
                var ssId: String? = null
                var uniqueMomIds: Set<String> = HashSet<String>(motherIds)
                for (mom in uniqueMomIds) {
                    //Split ID and Day
                    val thisMomsId = Integer.parseInt(mom.split("**")[0])
                    val thisMomsDate = SimpleDateFormat("yyyy-MM-dd").parse(mom.split("**")[1])

                    doAsync {

                        try {
                            ssId = provideSSid(thisMomsId)
                            if (ssId != null) {
                                var p: Post = Post(
                                    ssId!!,
                                    thisMomsId,
                                    thisMomsDate,
                                    "https://www.tinygraphs.com/squares/$ssId?theme=heatwave&numcolors=4&size=50&fmt=png",
                                    StandStrong.applicationContext().getString(ct),
                                    ssId!!,
                                    drawable,
                                    false,
                                    0,
                                    type,
                                    StandStrong.applicationContext().getString(pstTitle) + " " + ssId,
                                    StandStrong.applicationContext().getString(pstTxt)
                                )

                                database.postDao().insertPost(p)
                            }
                        } catch (e: HttpException) {
                            Crashlytics.logException(e)
                        } catch (e: Throwable) {
                            Crashlytics.logException(e)
                        } catch (e: Exception) {
                            Crashlytics.logException(e)
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun checkForNewProximity() {
            GlobalScope.launch(Dispatchers.Main) {
                val motherIds = ArrayList<String>()
                val request = endpoints!!.getProximityDataAsync(getLastProxyRow(PROXIMITY), limit = ITEMS_LIMIT)
                try {
                    val response = request.await()

                    for (r in response.body().orEmpty()) {
                        if(r.chartEvent.toString() == "Visibility") {
                            /*"chartDay": "2018-11-08",
                            "chartHour": "18",*/
                            val dt = r.chartDay +  " " + r.chartHour + ":00"
                            motherIds.add(Integer.toString(r.motherId!!)+"**"+r.chartDay)
                            val date =   SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dt)
                            val proxy : Proximity = Proximity(r.chartEvent,r.chartValue,r.motherId,date,r.proximityId)
                            withContext(Dispatchers.IO) {
                                database.proximityDao().insertProximity(proxy)
                            }
                        }
                        updateLastRetrievedRow(PROXIMITY, r.proximityId!!)
                    }
                    if (response.body().orEmpty().isNotEmpty()) {
                        createDataPost(motherIds, R.string.card_title_proximity, DATA_ROUTINE, R.string.post_title_proximity, R.string.post_subtitle_proximity,StandStrong.POST_CARD_PROXIMITY)
                        checkForNewProximity()
                    }
                } catch (e: HttpException) {
                    Crashlytics.logException(e)
                } catch (e: Throwable) {
                    Crashlytics.logException(e)
                } catch (e: Exception) {
                    Crashlytics.logException(e)
                }
            }

        }

        @JvmStatic fun checkForNewGPS() {
            GlobalScope.launch(Dispatchers.Main) {
                val motherIds = ArrayList<String>()
                val request = endpoints!!.getGPSDataAsync(getLastRow(GPS), limit = ACTIVITY_LIMIT)
                try {
                    val response = request.await()
                    val gpssArr = response.body().orEmpty()
                    for (r in gpssArr) {
                        motherIds.add(Integer.toString(r.mother!!.id!!)+"**"+ r.captureDate!!.split(" ")[0])
                        //"captureDate": "2019-02-01 13:10:19",
                        val date =   SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(r.captureDate)
                        var gps : Gps = Gps(r.latitude,r.longitude,r.altitude,r.accuracy, r.mother!!.id,date)
                        withContext(Dispatchers.IO) {
                            database.gpsDao().insertGPS(gps)
                        }
                        updateLastRetrievedRow(GPS, r.id!!)
                    }
                    if (response.body().orEmpty().isNotEmpty()) {
                        createDataPost(motherIds, R.string.card_title_gps,DATA_GPS,R.string.post_title_gps,R.string.post_subtitle_gps,StandStrong.POST_CARD_GPS)
                        checkForNewGPS()
                    }
                } catch (e: HttpException) {
                    Crashlytics.logException(e)
                } catch (e: Throwable) {
                    Crashlytics.logException(e)
                } catch (e: Exception) {
                    Crashlytics.logException(e)
                }
            }
        }

        @JvmStatic fun checkForNewActivity() {
            GlobalScope.launch(Dispatchers.Main) {
                val motherIds = ArrayList<String>()
                val request = endpoints!!.getActivityDataAsync(getLastRow(ACTIVITY), limit = ACTIVITY_LIMIT)
                try {
                    val response = request.await()
                    val activities = response.body().orEmpty()
                    for (r in activities) {
                        motherIds.add(Integer.toString(r.mother!!.id!!)+"**"+ r.captureDate!!.split(" ")[0])
                        //"captureDate": "2019-02-01 13:10:19",
                        val date =   SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(r.captureDate)
                        var act : Activity= Activity(r.activityType,r.confidence,r.mother!!.id!!,date,r.id)
                        withContext(Dispatchers.IO) {
                            database.activityDao().insertActivity(act)
                        }
                        updateLastRetrievedRow(ACTIVITY, r.id!!)
                    }
                    if (response.body().orEmpty().isNotEmpty()) {
                        createDataPost(motherIds, R.string.card_title_activity, DATA_ACTIVITY,R.string.post_title_activity,R.string.post_subtitle_activity,StandStrong.POST_CARD_ACTIVITY)
                    }

                    if (activities.isNotEmpty())
                        checkForNewActivity()

                } catch (e: HttpException) {
                    Crashlytics.logException(e)
                } catch (e: Throwable) {
                    Crashlytics.logException(e)
                } catch (e: Exception) {
                    Crashlytics.logException(e)
                }
            }
        }

        @JvmStatic private fun getSsId(momId:Int):String{
            var ssId = ""
            //GlobalScope.launch(Dispatchers.Main) {

                runBlocking {
                    try {
                        val request = endpoints!!.getMotherByIdAsync(momId)
                        val response = request.await()
                        val mother = response.body()!!
                        ssId = mother.identificationNumber!!
                        var imgUrl = "https://www.tinygraphs.com/squares/"+mother.identificationNumber.toString()+"?theme=heatwave&numcolors=4&size=50&fmt=png"
                        var p: Person = Person(mother.identificationNumber.toString(),mother.firstName.toString(),mother.lastName.toString(),  mother.id!!,1,imgUrl)
                        val res = database.personDao().insertPerson(p)
                        Log.d(TAG, "mom inserted $res")
                    } catch (e: HttpException) {
                        Log.d("SSS",e.code().toString())
                    } catch (e: Throwable) {
                        Log.d("SSS",e.toString())
                    }
                }
            //}
            return ssId
        }

        @JvmStatic private fun updateLastRetrievedRow(table:String, rowId:Int) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
            val editor = sharedPref.edit()
            editor.putInt(table,rowId)
            editor.apply()
        }

        @JvmStatic private fun getLastRow(table:String):String{
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
            return "id>" + Integer.toString(sharedPref!!.getInt(table,0)) //start on first row
        }

        @JvmStatic private fun getLastProxyRow(table:String):Int{
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext())
            return sharedPref!!.getInt(table,0) //start on first row
        }

        @JvmStatic fun getAllAwards() {

            doAsync {

                    endpoints!!.getAwardsList(getLastRow(AWARDS), 0, ITEMS_LIMIT).enqueue(object : Callback<List<ApiAward>> {
                        override fun onFailure(call: Call<List<ApiAward>>, t: Throwable) {

                            Log.e(TAG, "error while get awards: " + Log.getStackTraceString(t))
                        }

                        override fun onResponse(call: Call<List<ApiAward>>, response: Response<List<ApiAward>>) {

                            Log.d(TAG, "loaded awards ${response.body()?.size}")

                            val errorString = response.errorBody()?.string()

                            if (errorString.isNullOrEmpty()) {

                                Log.d(TAG, "error str $errorString")

                                val awards = response.body()
                                val awardsToSave = mutableListOf<Award>()
                                val postsToSave = mutableListOf<Post>()

                                if (!awards.isNullOrEmpty()) {

                                    doAsync {
                                        for (award in awards) {

                                            var ssId = provideSSid(award.mother!!.id!!)

                                            if (ssId != null) {
                                                var a: Award = Award(
                                                    award.mother!!.id!!,
                                                    SimpleDateFormat("yyyy-MM-dd").parse(award.awardForDate),
                                                    ssId,
                                                    "@drawable/" + switchAward(award.awardType!!) + "_l" + Integer.toString(award.awardLevel!!))
                                                var p: Post = Post(
                                                    ssId,
                                                    a.motherId,
                                                    Date(),
                                                    "https://www.tinygraphs.com/squares/" + a.motherId + "?theme=heatwave&numcolors=4&size=50&fmt=png",
                                                    StandStrong.applicationContext().getString(R.string.card_title_award),
                                                    ssId,
                                                    "@drawable/" + switchAward(award.awardType!!) + "_l" + Integer.toString(award.awardLevel!!),
                                                    false,
                                                    0,
                                                    StandStrong.POST_CARD_AWARD,
                                                    ssId + " " + StandStrong.applicationContext().getString(R.string.post_title_award),
                                                    StandStrong.applicationContext().getString(R.string.post_subtitle_award), a.awardId
                                                )

                                                awardsToSave.add(a)
                                                postsToSave.add(p)
                                            }
                                        }

                                        database.awardDao().insertAll(awardsToSave)
                                        database.postDao().insertAll(postsToSave)
                                        updateLastRetrievedRow(AWARDS, awards.last().id!!)

                                        if (awards.isNotEmpty()) {
                                            getAllAwards()
                                        }
                                    }
                                }
                            } else {

                                //trying error parse
                                val error = Gson().fromJson(errorString, ErrorModel::class.java)

                                Log.d(TAG, "error $error")

                                if (error.status==401) {

                                    login("1111", Const.TEMP_PASS)
                                }
                            }
                        }
                    })
            }
        }

        private fun provideSSid(motherId: Int?): String? {

            if (motherId == null)
                return null

            var ssId = database.personDao().getPersonByMotherIdSync(motherId)?.ssId

            if (ssId.isNullOrEmpty()) {

                ssId = getSsId(motherId)
            }

            return ssId
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

            doAsync {

                val m: Message? = database.messageDao().getMessageById(id)

                Log.d(TAG, "message from db $m")

                if (m != null) {
                    uploadMessage(ApiMessage(m))
                }
            }
        }

        @JvmStatic fun uploadMessage(message : ApiMessage) {

            var endpoints: ApiEndpoints? = ApiService.service

            message.status = "PENDING"

            doAsync {
                endpoints?.sendMessageToServer(message)?.enqueue(object : Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful) {
                        Log.d("SSTRNG","Success")
                    } else {
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

        @JvmStatic fun checkForNewMessages() {

            doAsync {

                endpoints!!.getMessagesAsync(getLastRow(MESSAGES), limit = ITEMS_LIMIT).enqueue(object : Callback<List<ApiMessage>> {
                    override fun onFailure(call: Call<List<ApiMessage>>, t: Throwable) {

                        Log.e(TAG, "error while get messages " + Log.getStackTraceString(t))
                    }

                    override fun onResponse(call: Call<List<ApiMessage>>, response: Response<List<ApiMessage>>) {

                        val motherIds = ArrayList<Int>()
                        val apiMessages = response.body().orEmpty()

                        for (apiMessage in apiMessages) {

                            Log.d(TAG, "r $apiMessage")

                            apiMessage.mother!!.id?.let { motherIds.add(it) }

                            saveMessage(apiMessage)

                            updateLastRetrievedRow(MESSAGES, apiMessage.id!!)

                            var uniqueMomIds: Set<Int> = HashSet<Int>(motherIds)

                            if(uniqueMomIds.isNotEmpty())
                                triggerNotification(999,StandStrong.applicationContext().resources.getString(R.string.notification_description_message))
                        }

                        if (apiMessages.isNotEmpty())
                            checkForNewMessages()
                    }

                })
            }
        }

        private fun saveMessage(apiMessage: ApiMessage) {

                doAsync {

                    var ssId = provideSSid(apiMessage.mother!!.id)

                    if (ssId != null) {
                        var threadId = apiMessage.threadId!!
                        val dateMsg = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(apiMessage.postedDate)


                        if (apiMessage.direction == StandStrong.MESSAGE_DIRECTION_IN) {
                            createMessagePostLocally(apiMessage, ssId, dateMsg)
                        }

                        val m = Message(
                            apiMessage.mother!!.id!!,
                            apiMessage.message!!,
                            apiMessage.direction!!,
                            threadId,
                            dateMsg
                        )

                        try {
                            database.messageDao().insertMessage(m)
                            Log.d(TAG, "message inserted $m")
                        } catch (e: Exception) {
                            Log.d("SSS", "Error")
                        }
                    }
                }
        }

        private fun createMessagePostLocally(r: ApiMessage, ssId: String, dateMsg: Date): Int {

            //"postedDate": "2019-02-06 00:00:00",
            var cardTitle = StandStrong.applicationContext().getString(R.string.card_title_msg)
            var mURL = NEW_MESSAGE_DRAWABLE
            var aURL = "https://www.tinygraphs.com/squares/"+r.mother!!.id!!+"?theme=heatwave&numcolors=4&size=50&fmt=png"
            var postTitle = StandStrong.applicationContext().getString(R.string.post_title_msg)
            var postTxt =StandStrong.applicationContext().getString(R.string.post_subtitle_msg)
            var p: Post = Post( ssId, r.mother!!.id!!,dateMsg,aURL,cardTitle,ssId,mURL,false,0,StandStrong.POST_CARD_MESSAGE,postTitle,postTxt)

            Log.d(TAG, "post inserted " + p.printThis())

            return database.postDao().insertPost(p).toInt()
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
