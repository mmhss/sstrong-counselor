package com.hsd.avh.standstrong.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.fragments.PeopleFragmentDirections

/**
 * The ViewModel used in [PersonDetailFragment].
 */
class PersonDetailViewModel(
        personRepository: PersonRepository,
        private val personId: String
) : ViewModel() {

    val person: LiveData<Person>

    init {

        person = personRepository.getPersonById(personId)

    }

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
