package com.hsd.avh.standstrong.data.posts

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.people.Person
import java.util.*

/**
 * The Data Access Object for the Proximity class.
 */
@Dao
interface ProximityDao {
    @Query("SELECT * FROM proximity ORDER BY chart_date")
    fun getProximity(): LiveData<List<Proximity>>

    @Query("SELECT * FROM proximity where chart_event='Visibility' and mother_id = :motherId and chart_date BETWEEN :dayst AND :dayet ORDER BY chart_date")
    fun getProximityByDate(motherId:Int,dayst:Long,dayet:Long): LiveData<List<Proximity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProximity(proxy: Proximity): Long

    @Delete
    fun deleteProximity(proxy: Proximity)

    @Query("DELETE FROM proximity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(proximity: List<Proximity>)

}
