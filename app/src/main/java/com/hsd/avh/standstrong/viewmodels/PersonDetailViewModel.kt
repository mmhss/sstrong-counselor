package com.hsd.avh.standstrong.viewmodels

import android.util.Log
import android.view.View
import androidx.collection.ArrayMap
import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository
import com.hsd.avh.standstrong.utilities.SSUtils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PersonDetailViewModel(
        private val personRepository: PersonRepository,
        personId: String
) : ViewModel() {


    private val postList = MediatorLiveData<List<Post>>()
    private var awardCount = MediatorLiveData<Int>()
    private var messageCount = MediatorLiveData<Int>()
    private var postCount = MediatorLiveData<Int>()
    val person: LiveData<Person> = personRepository.getPersonById(personId)
    private val postFilterList = MutableLiveData<Int>()

    private lateinit var appliedFilters : ArrayMap<String, List<String>>
    init {
        val livePostList = personRepository.getPostListById(personId)
        val liveAwardCount = personRepository.getAwardCount(personId)
        val liveMessageCount = personRepository.getMessageCount(personId)
        val livePostCount = personRepository.getPostCount(personId)
        postFilterList.value = PersonDetailViewModel.NO_FILTER

        if(StandStrong.isNotRA()) {
            //val livePostList = postRepository.getPosts()
            //postList.addSource(livePostList, postList::setValue)

            var sDate: Long =-1
            var eDate: Long =-1
            val livePostList = Transformations.switchMap(postFilterList) {
                if (it == NO_FILTER) {
                    personRepository.getPostListById(personId)
                } else {
                    var fl : ArrayList<Int> = ArrayList<Int>()
                    var q: String = "SELECT * FROM posts WHERE person_id = '$personId' "
                    var hasFilters : Boolean = false
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
                        if(k != "date") {
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
                    if(sDate > 0 ) {
                        "$q AND date BETWEEN $sDate AND $eDate ORDER BY date desc"
                    } else {
                        "$q ORDER BY date desc"
                    }
                    val query = SimpleSQLiteQuery(q)

                    personRepository.getPostListByIdAndFilters(query)
                }
            }
            postList.addSource(livePostList, postList::setValue)
        } else {
            val livePostList = personRepository.getRAPostListById(personId)
            postList.addSource(livePostList, postList::setValue)
        }



        awardCount.addSource(liveAwardCount, awardCount::setValue)
        messageCount.addSource(liveMessageCount, messageCount::setValue)
        postCount.addSource(livePostCount, postCount::setValue)

    }

    fun getPosts() = postList

    fun getAwardCount() = awardCount
    fun getMessageCount() = messageCount
    fun getPostCount() = postCount

    fun newMessage(msg :String) {
        runBlocking {
        withContext(IO) {
            lateinit var immPerson : Person
            runBlocking {
                immPerson = personRepository.getImmutablePersonById("personId")
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
                    personRepository.insertMessage(m)
                }
            } catch (e: Exception) {
                Log.d("SSS", "Error")
            }

        }
        }
    }
    fun setFilter(filter: ArrayMap<String, List<String>>) {
        //ArrayMap<String, List<String>> applied_filters = new ArrayMap<>();
        appliedFilters = filter

        postFilterList.value = Random().nextInt(20000 )
    }

    fun clearFilter() {
        postFilterList.value = PersonDetailViewModel.NO_FILTER
    }

    fun isFiltered() = postFilterList.value != PersonDetailViewModel.NO_FILTER



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
