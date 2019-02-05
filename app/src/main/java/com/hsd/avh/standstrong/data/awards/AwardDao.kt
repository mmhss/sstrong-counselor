package com.hsd.avh.standstrong.data.awards

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object for the [Award] class.
 */
@Dao
interface AwardDao {
    @Query("SELECT * FROM awards")
    fun getAwards(): LiveData<List<Award>>

    @Query("SELECT * FROM awards WHERE id = :awardId")
    fun getAwardById(awardId: String): LiveData<Award>

    @Query("SELECT * FROM awards WHERE mother_id = :personId")
    fun getAwardsForPerson(personId: String): LiveData<Award>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAward(award: Award): Long

    @Delete
    fun deleteAward(award: Award)

    @Query("DELETE FROM awards")
    fun deleteAll()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(awards: List<Award>)
}