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









class PersonRepository private constructor(
    private val personDao: PersonDao
) {

    private var endpoints: ApiEndpoints? = ApiService.service
    fun refreshPersonList() {
        endpoints?.getMother()?.enqueue(object : Callback<List<ApiPerson>> {
            override fun onResponse(call: Call<List<ApiPerson>>, response: Response<List<ApiPerson>>) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", response.body().toString())
                    for (mother in response.body().orEmpty()) {
                        var p:Person = Person(mother.identificationNumber.toString(),"",1,"https://robohash.org/nonquibusdamipsam.png?size=50x50&set=set1")
                        CoroutineScope(Dispatchers.IO).launch {
                            createPerson(p)
                        }
                    }
                } else {
                    val statusCode = response.code()
                    // handle request errors depending on status code
                    Log.d("MainActivity", statusCode.toString())
                }
            }

            override fun onFailure(call: Call<List<ApiPerson>>, t: Throwable) {

                Log.d("MainActivity", "error loading from API")

            }
        })
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
            personDao.getPersonById(personId)

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
