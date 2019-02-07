package com.hsd.avh.standstrong.data.posts

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.people.Person

/**
 * The Data Access Object for the Activity class.
 */
@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity ")
    fun getActivity(): LiveData<List<Activity>>

    @Insert
    fun insertActivity(activity: Activity)

    @Delete
    fun deleteActivity(activity: Activity)

    @Query("DELETE FROM activity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(activity: List<Activity>)
}
