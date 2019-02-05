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
    fun getProximity(): LiveData<List<ApiProximity>>

    @Insert
    fun insertProximity(proxy: ApiProximity): Long

    @Delete
    fun deleteProximity(proxy: ApiProximity)

    @Query("DELETE FROM proximity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(proximity: List<ApiProximity>)

}
