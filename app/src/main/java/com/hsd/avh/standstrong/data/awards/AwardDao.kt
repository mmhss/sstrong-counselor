package com.hsd.avh.standstrong.data.awards

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Deferred

/**
 * The Data Access Object for the [Award] class.
 */
@Dao
interface AwardDao {
    @Query("SELECT * FROM awards")
    fun getAwards(): LiveData<List<Award>>

    @Query("SELECT count(id) FROM awards")
    fun countAwards(): Int

    @Query("SELECT * FROM awards WHERE id = :awardId")
    fun getAwardById(awardId: String): LiveData<Award>

    @Query("SELECT * FROM awards WHERE mother_id = :personId")
    fun getAwardsForPerson(personId: String): LiveData<Award>

    @Query("SELECT count(*) FROM posts WHERE person_id = :personId")
    fun getAwardCountForPerson(personId: String): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAward(award: Award): Long

    @Delete
    fun deleteAward(award: Award)

    @Query("DELETE FROM awards")
    fun deleteAll()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(awards: List<Award>)

    @Query("SELECT * FROM awards")
    fun getAwardsList(): List<Award>
}