package com.hsd.avh.standstrong.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository

class PersonDetailViewModel(
        personRepository: PersonRepository,
        personId: String
) : ViewModel() {


    private val postList = MediatorLiveData<List<Post>>()
    private var awardCount = MediatorLiveData<Int>()
    private var messageCount = MediatorLiveData<Int>()
    private var postCount = MediatorLiveData<Int>()
    val person: LiveData<Person> = personRepository.getPersonById(personId)


    init {
        val livePostList = personRepository.getPostListById(personId)
        val liveAwardCount = personRepository.getAwardCount(personId)
        val liveMessageCount = personRepository.getMessageCount(personId)
        val livePostCount = personRepository.getPostCount(personId)

        //postList.addSource(livePostList, postList::setValue)
        if(StandStrong.isNotRA()) {
            val livePostList = personRepository.getPostListById(personId)
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
}
