package com.hsd.avh.standstrong.data.posts

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award

/**
 * The Data Access Object for the Counsellor class.
 */
@Dao
interface SupervisorDao {

    @Query("SELECT counsellor_id FROM supervisors WHERE supervisor_id = :supervisorId")
    fun getCounsellorForSupervisor(supervisorId: String): LiveData<Supervisor>

    @Insert
    fun insertSupervisorRelationship(supervisor: Supervisor): Long

    @Delete
    fun deleteSupervisorRelationship(supervisor: Supervisor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRelationships(supervisor: List<Supervisor>)
}
