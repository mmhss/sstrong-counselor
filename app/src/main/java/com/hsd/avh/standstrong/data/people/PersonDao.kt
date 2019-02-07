package com.hsd.avh.standstrong.data.people

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsd.avh.standstrong.data.posts.Post

/**
 * The Data Access Object for the [Person] class.
 */
@Dao
interface PersonDao {
    @Query("SELECT * FROM people ORDER BY ss_id")
    fun getAllPeople(): LiveData<List<Person>>

    @Query("SELECT count(id) FROM people")
    fun countPeople(): Int

    @Query("SELECT * FROM people WHERE mother_id = :motherId")
    fun getPersonByMotherId(motherId: Int): LiveData<Person>

    @Query("SELECT * FROM people WHERE ss_id = :ssId")
    fun getPersonByMotherSsId(ssId: String): LiveData<Person>

    @Query("SELECT ss_id FROM people WHERE id = :motherId")
    fun getMotherSsId(motherId: Int): String


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