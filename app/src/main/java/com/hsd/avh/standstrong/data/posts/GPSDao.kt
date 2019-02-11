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
    fun getGPS(): LiveData<List<Gps>>

    @Query("SELECT latitude,longitude FROM gps where mother_id = :motherId and accuracy < :accuracy and record_date BETWEEN :dayst AND :dayet ORDER BY record_date")
    fun getGpsByDate(motherId:Int,accuracy:Int,dayst:Long,dayet:Long): LiveData<List<Gps>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGPS(gps: Gps)

    @Delete
    fun deleteGPS(gps: Gps)

    @Query("DELETE FROM gps")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gps: List<Gps>)
}
