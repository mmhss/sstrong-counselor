package com.hsd.avh.standstrong.data.posts

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.people.Person
import java.util.*

/**
 * The Data Access Object for the Activity class.
 */
@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity ")
    fun getActivity(): LiveData<List<Activity>>

    @Query("SELECT * FROM activity where mother_id = :motherId and confidence > :confidence and capture_date BETWEEN :dayst AND :dayet ORDER BY capture_date")
    fun getActivityByDate(motherId:Int,confidence:Int,dayst:Long, dayet: Long): LiveData<List<Activity>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertActivity(activity: Activity)

    @Delete
    fun deleteActivity(activity: Activity)

    @Query("DELETE FROM activity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(activity: List<Activity>)
}
