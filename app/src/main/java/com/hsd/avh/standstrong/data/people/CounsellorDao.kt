package com.hsd.avh.standstrong.data.posts

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award

/**
 * The Data Access Object for the Counsellor class.
 */
@Dao
interface CounsellorDao {

    @Query("SELECT person_id FROM counsellors WHERE counsellor_id = :counsellorId")
    fun getPeopleForCounsellor(counsellorId: String): LiveData<Counsellor>

    @Insert
    fun insertCounsellorRelationship(counsellor: Counsellor): Long

    @Delete
    fun deleteCounsellorRelationship(counsellor: Counsellor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRelationships(counsellor: List<Counsellor>)
}
