package com.hsd.avh.standstrong.data.awards

import com.hsd.avh.standstrong.utilities.SSUtils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

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