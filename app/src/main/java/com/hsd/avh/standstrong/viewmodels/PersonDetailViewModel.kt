package com.hsd.avh.standstrong.viewmodels

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import android.view.View
import androidx.collection.ArrayMap
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.messages.ApiMessage
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository
import com.hsd.avh.standstrong.utilities.Const
import com.hsd.avh.standstrong.utilities.SSUtils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PersonDetailViewModel(
        private val personRepository: PersonRepository,
        val personId: String
) : ViewModel() {


    private var awardCount = MediatorLiveData<Int>()
    private var messageCount = MediatorLiveData<Int>()
    private var postCount = MediatorLiveData<Int>()
    val person: LiveData<Person> = personRepository.getPersonById(personId)
    private var pid : String =""
    private val postFilterList = MutableLiveData<Int>()
    private var appliedFilters : ArrayMap<String, List<String>> = ArrayMap<String, List<String>>()
    val TAG = javaClass.canonicalName
    private val scrollToTop = MutableLiveData<Boolean>()

    init {
        setPid(personId)
        val livePostList = personRepository.getPostListById(personId)
        val liveAwardCount = personRepository.getAwardCount(personId)
        val liveMessageCount = personRepository.getMessageCount(personId)
        val livePostCount = personRepository.getPostCount(personId)
        postFilterList.value = PersonDetailViewModel.NO_FILTER

        awardCount.addSource(liveAwardCount, awardCount::setValue)
        messageCount.addSource(liveMessageCount, messageCount::setValue)
        postCount.addSource(livePostCount, postCount::setValue)

    }

    private val pagedPersonPosts: LiveData<PagedList<Post>> = Transformations.switchMap(postFilterList) {

        Log.d(TAG, "post filter ${postFilterList.value}")

        if(StandStrong.isNotRA()) {

            var sDate: Long =-1
            var eDate: Long =-1

            var fl : ArrayList<Int> = ArrayList<Int>()
            var q: String = "SELECT * FROM posts WHERE "
            var hasFilters : Boolean = false

            Log.d(TAG, "applied $appliedFilters")

            if(!appliedFilters.isNullOrEmpty()){
                q = "$q person_id = '" + appliedFilters["pid"]!![0].toString() +"' "
                for ((k, v) in appliedFilters) {
                    if(k == "date") {
                        var stringDate = appliedFilters["date"]!![0].toString()
                        val format = SimpleDateFormat("yyyy-MM-dd")
                        try {
                            val date = format.parse(stringDate)
                            sDate = setTime(date.time,0,0,0)
                            eDate = setTime(date.time,23,59,59)
                            System.out.println(date)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    if(k != "date" && k != "pid") {
                        hasFilters = true
                        q = "$q AND type IN ("
                        v.forEach { filterItem ->
                            when (filterItem) {
                                StandStrong.POST_CARD_STRING_MESSAGE -> q = q + StandStrong.POST_CARD_MESSAGE + ","
                                StandStrong.POST_CARD_STRING_PROXIMITY -> q = q + StandStrong.POST_CARD_PROXIMITY+ ","
                                StandStrong.POST_CARD_STRING_GPS -> q = q + StandStrong.POST_CARD_GPS+ ","
                                StandStrong.POST_CARD_STRING_ACTIVITY -> q = q + StandStrong.POST_CARD_ACTIVITY+ ","
                                StandStrong.POST_CARD_STRING_AWARD -> q = q + StandStrong.POST_CARD_AWARD+ ","
                                StandStrong.POST_CARD_STRING_CONTENT -> q = q + StandStrong.POST_CARD_CONTENT+ ","
                                StandStrong.POST_CARD_STRING_GOAL -> q = q + StandStrong.POST_CARD_GOAL+ ","
                            }
                        }
                        if(q.substring(q.length - 1) == ",") {
                            q = q.substring(0, q.length - 1);
                            q = "$q)"
                        }
                    }
                }
                q = if(sDate > 0) {
                    "$q AND date BETWEEN $sDate AND $eDate ORDER BY date desc"
                } else {
                    "$q ORDER BY date desc"
                }

                val query = SimpleSQLiteQuery(q)

                Log.d(TAG, "taking by query $q")

                return@switchMap LivePagedListBuilder<Int, Post>(personRepository.getPostListByIdAndFiltersPaged(query), Const.PAGE_SIZE).build()
            } else {

                Log.d(TAG, "taking all ")
                return@switchMap LivePagedListBuilder<Int, Post>(personRepository.getPostListByIdPaged(personId), Const.PAGE_SIZE).build()
            }
        } else {

            Log.d(TAG, "showing RA")
            return@switchMap LivePagedListBuilder<Int, Post>(personRepository.getRAPostListByIdPaged(personId), Const.PAGE_SIZE).build()
        }
    }

    fun getPersonPostsPaged() = pagedPersonPosts

    fun subscribeOnScrollToTop() : LiveData<Boolean> = scrollToTop

    fun setPid(id:String) {
        pid = id
    }
    fun getAwardCount() = awardCount
    fun getMessageCount() = messageCount
    fun getPostCount() = postCount

    fun newMessage(msg :String) {
        runBlocking {
            withContext(IO) {
                lateinit var immPerson : Person
                runBlocking {
                    immPerson = personRepository.getImmutablePersonById(pid)
                }
                val dateMsg = Date()
                var cardTitle = StandStrong.applicationContext().getString(com.hsd.avh.standstrong.R.string.card_title_msg)
                var mURL = SSUtils.NEW_MESSAGE_DRAWABLE
                var aURL = "https://www.tinygraphs.com/squares/" + immPerson.mother_id + "?theme=heatwave&numcolors=4&size=50&fmt=png"
                var postTitle = StandStrong.applicationContext().getString(com.hsd.avh.standstrong.R.string.post_title_msg)
                var postTxt = StandStrong.applicationContext().getString(com.hsd.avh.standstrong.R.string.post_subtitle_msg)
                var p: Post = Post(immPerson.ssId, immPerson.mother_id, dateMsg, aURL, cardTitle, immPerson.ssId, mURL, false, 0, StandStrong.POST_CARD_MESSAGE, postTitle, postTxt)
                var insertRowIdRow: Long = 0
                try {
                    runBlocking {
                        insertRowIdRow = personRepository.insertPost(p)
                        val m = Message(immPerson.mother_id, msg, StandStrong.MESSAGE_DIRECTION_OUT, insertRowIdRow.toInt(), dateMsg)
                        val apiMessage = ApiMessage(m.msg, SimpleDateFormat(Const.MESSAGE_DATE_FORMAT).format(m.msgDate), m.msgThread, m.direction, ApiMessage.Mother(immPerson.mother_id))
                        personRepository.insertMessage(m)
                        SSUtils.uploadMessage(apiMessage)
                        scrollToTop.postValue(true)
                    }
                } catch (e: Exception) {
                    Log.d("SSS", "Error")
                }
        }
        }
    }

    fun newGoal(msg :String) {
        runBlocking {
            withContext(IO) {
                lateinit var immPerson : Person
                runBlocking {
                    immPerson = personRepository.getImmutablePersonById(pid)
                }
                val dateMsg = Date()
                var cardTitle = StandStrong.applicationContext().getString(com.hsd.avh.standstrong.R.string.card_title_goal)
                var mURL = SSUtils.GOAL_DRAWABLE
                var aURL = "https://www.tinygraphs.com/squares/" + immPerson.mother_id + "?theme=heatwave&numcolors=4&size=50&fmt=png"
                var postTitle = StandStrong.applicationContext().getString(com.hsd.avh.standstrong.R.string.post_title_goal)
                var postTxt = msg
                var p: Post = Post(immPerson.ssId, immPerson.mother_id, dateMsg, aURL, cardTitle, immPerson.ssId, mURL, false, 0, StandStrong.POST_CARD_MESSAGE, postTitle, postTxt)
                try {
                    runBlocking {
                        personRepository.insertPost(p)
                    }
                } catch (e: Exception) {
                    Log.d("SSS", "Error")
                }
            }
        }
    }

    fun setFilter(filter: ArrayMap<String, List<String>>, pid : String) {
        //ArrayMap<String, List<String>> applied_filters = new ArrayMap<>();
        appliedFilters = filter
        val temp = ArrayList<String>()
        temp.add(pid)
        appliedFilters["pid"] = null
        appliedFilters["pid"] = temp

        Log.d(TAG, "filters $appliedFilters")

        postFilterList.value = Random().nextInt(20000 )
    }

    fun updatePostList(ppid : String) {
        val temp = ArrayList<String>()
        temp.add(ppid)
        appliedFilters["pid"] = null
        appliedFilters["pid"] = temp
        setPid(ppid)
        postFilterList.value = Random().nextInt(20000 )
    }

    fun clearFilter() {
        appliedFilters = ArrayMap<String,List<String>>()
    }

    fun isFiltered() = appliedFilters.isNullOrEmpty()



    fun clickListenerPosts() : View.OnClickListener {
        return View.OnClickListener {

            /*val bundle = Bundle()
            bundle.putString("person_id", personId)
            mFirebaseAnalytics?.logEvent("viewed_person", bundle)

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "be9e5ff91543")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Organize your Analytics code")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "article")
            FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
            */
        /*    val direction = PersonD PeopleFragmentDirections. .actionPeopleListToPersonDetail(personId)
            it.findNavController().navigate(direction)*/
        }
    }

    fun clickListenerMsgs() : View.OnClickListener {
        return View.OnClickListener {

            /*val bundle = Bundle()
            bundle.putString("person_id", personId)
            mFirebaseAnalytics?.logEvent("viewed_person", bundle)

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "be9e5ff91543")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Organize your Analytics code")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "article")
            FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
            */
            /*    val direction = PersonD PeopleFragmentDirections. .actionPeopleListToPersonDetail(personId)
                it.findNavController().navigate(direction)*/
        }
    }

    fun clickListenerAwards() : View.OnClickListener {
        return View.OnClickListener {

            /*val bundle = Bundle()
            bundle.putString("person_id", personId)
            mFirebaseAnalytics?.logEvent("viewed_person", bundle)

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "be9e5ff91543")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Organize your Analytics code")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "article")
            FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
            */
            /*    val direction = PersonD PeopleFragmentDirections. .actionPeopleListToPersonDetail(personId)
                it.findNavController().navigate(direction)*/
        }
    }

    private fun setTime(dateInMill: Long,h:Int,m:Int,s:Int): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = dateInMill
        cal.set(Calendar.HOUR_OF_DAY, h)
        cal.set(Calendar.MINUTE, m)
        cal.set(Calendar.SECOND, s)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    companion object {
        private const val NO_FILTER = -1
    }
}
