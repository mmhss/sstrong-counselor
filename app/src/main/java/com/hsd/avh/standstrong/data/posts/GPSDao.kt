package com.hsd.avh.standstrong.data.posts

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.people.Person

/**
 * The Data Access Object for the GPS class.
 */
@Dao
interface GPSDao {
    @Query("SELECT * FROM gps ORDER BY record_date")
    fun getGPS(): LiveData<List<ApiGPS>>

    @Insert
    fun insertGPS(gps: ApiGPS)

    @Delete
    fun deleteGPS(gps: ApiGPS)

    @Query("DELETE FROM gps")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gps: List<ApiGPS>)
}
