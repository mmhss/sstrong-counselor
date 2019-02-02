package com.hsd.avh.standstrong.data.people

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.posts.Post

/**
 * The Data Access Object for the [Person] class.
 */
@Dao
interface PersonDao {
    @Query("SELECT * FROM people")
    fun getAllPeople(): LiveData<List<Person>>

    @Query("SELECT * FROM people WHERE id = :personId")
    fun getPersonById(personId: String): LiveData<Person>

    @Query("SELECT count(*) FROM people")
    fun getPersonCount(): LiveData<Int>




    /**
     * This query will tell Room to query both the [Award] and [Post] tables and handle
     * the object mapping.
     */
//    @Transaction
//    @Query("SELECT * FROM people")
//    fun getPlantAndGardenPlantings(): LiveData<List<PlantAndGardenPlantings>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPerson(person: Person): Long

    @Delete
    fun deletePerson(person: Person)



    @Query("DELETE FROM people")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(people: List<Person>)
}