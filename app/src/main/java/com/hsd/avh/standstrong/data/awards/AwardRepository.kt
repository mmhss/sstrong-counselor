package com.hsd.avh.standstrong.data.awards

import android.util.Log
import com.hsd.avh.standstrong.api.ApiEndpoints
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AwardRepository private constructor(
    private val awardDao: AwardDao
) {

    private var endpoints: ApiEndpoints? = ApiService.service
    fun refreshAwardList() {
        endpoints?.getAwards()?.enqueue(object : Callback<List<ApiAward>> {
            override fun onResponse(call: Call<List<ApiAward>>, response: Response<List<ApiAward>>) {
                if (response.isSuccessful) {
                    for (award in response.body().orEmpty()) {
                        val date = Date()
                        //TODO Fix this line
                        var a: Award = Award("SS1001",date,"@drawable/sc_l1_n","सामाजिक समर्थन - स्तर 1")
                        CoroutineScope(IO).launch {
                            createAward(a)
                        }
                    }
                } else {
                    val statusCode = response.code()
                    // handle request errors depending on status code
                    Log.d("MainActivity", statusCode.toString())
                }
            }

            override fun onFailure(call: Call<List<ApiAward>>, t: Throwable) {

                Log.d("MainActivity", "error loading from API")

            }
        })
    }

    suspend fun createAward(award: Award) {
        withContext(IO) {
            awardDao.insertAward(award)
        }
    }

    suspend fun removeAward(award: Award) {
        withContext(IO) {
            awardDao.deleteAward(award)
        }
    }

    fun getAwardById(awardId: String) =
            awardDao.getAwardById(awardId)

    fun getAwards() = awardDao.getAwards()

    fun getAwardByPerson(personId: String) = awardDao.getAwardsForPerson(personId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AwardRepository? = null

        fun getInstance(awardDao: AwardDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: AwardRepository(awardDao).also { instance = it }
                }
    }
}