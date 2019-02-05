package com.hsd.avh.standstrong.data.people


import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.api.ApiEndpoints
import androidx.annotation.NonNull
import android.R.attr.name
import com.crashlytics.android.Crashlytics
import com.hsd.avh.standstrong.utilities.SSUtils
import com.hsd.avh.standstrong.workers.MyAPIException


class PersonRepository private constructor(
    private val personDao: PersonDao
) {

    fun refreshPersonList() {
        SSUtils.checkForNewPeople()
    }

    suspend fun createPerson(person: Person) {
        withContext(IO) {
            personDao.insertPerson(person)
        }
    }

    suspend fun removePerson(person: Person) {
        withContext(IO) {
            personDao.deletePerson(person)
        }
    }

    fun getPersonById(personId: String) =
            personDao.getPersonByMotherSsId(personId)

    fun getAllPeople() = personDao.getAllPeople()


    fun getPeopleCount() =
            personDao.getPersonCount()


    companion object {


        // For Singleton instantiation
        @Volatile private var instance: PersonRepository? = null

        fun getInstance(personDao: PersonDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: PersonRepository(personDao).also { instance = it }
                }


        }

    }


private fun <T> Call<T>.enqueue(callback: Callback<ApiPerson>) {

}
