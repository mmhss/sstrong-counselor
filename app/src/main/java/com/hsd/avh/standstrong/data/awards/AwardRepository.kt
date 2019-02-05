package com.hsd.avh.standstrong.data.awards

import android.util.Log
import com.hsd.avh.standstrong.api.ApiEndpoints
import com.hsd.avh.standstrong.api.ApiService
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.utilities.SSUtils
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

    fun refreshAwardList() {
      SSUtils.checkForNewAwards()
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