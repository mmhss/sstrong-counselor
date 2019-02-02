package com.hsd.avh.standstrong.data.posts

/**
 * Repository module for handling data operations.
 */
class SupervisorRepository private constructor(private val supervisorDao: SupervisorDao) {

    fun getCounsellorsBySupervisorId(supervisorId: String) = supervisorDao.getCounsellorForSupervisor(supervisorId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: SupervisorRepository? = null

        fun getInstance(supervisorDao: SupervisorDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: SupervisorRepository(supervisorDao).also { instance = it }
                }
    }
}
