package com.hsd.avh.standstrong.data.posts

/**
 * Repository module for handling data operations.
 */
class CounsellorRepository private constructor(private val counsellorDao: CounsellorDao) {

    fun getClientsByCounsellorId(counsellorId: String) = counsellorDao.getPeopleForCounsellor(counsellorId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: CounsellorRepository? = null

        fun getInstance(counsellorDao: CounsellorDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: CounsellorRepository(counsellorDao).also { instance = it }
                }
    }
}
