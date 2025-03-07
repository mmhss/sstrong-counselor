package com.hsd.avh.standstrong.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.stream.JsonReader
import com.hsd.avh.standstrong.data.AppDatabase
import com.google.gson.GsonBuilder


class SeedDatabaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        //val postType = object : TypeToken<List<Post>>() {}.type
        //val peopleType = object : TypeToken<List<Person>>() {}.type
        //val awardType = object : TypeToken<List<Award>>() {}.type

        val jsonReader: JsonReader? = null

        return try {
            //val inputPostStream = applicationContext.assets.open(TEMP_POST_DATA)
           // val inputPeopleStream = applicationContext.assets.open(TEMP_PEOPLE_DATA)
            //val inputAwardStream = applicationContext.assets.open(TEMP_AWARD_DATA)

            GsonBuilder().setDateFormat("yyyy-MM-dd").create()

            //People
            //jsonReader = JsonReader(inputPeopleStream.reader())
            val database = AppDatabase.getInstance(applicationContext)
           /* val peopleList: List<Person> = gsonBuilder.fromJson(jsonReader, peopleType)*/
            database.personDao().deleteAll()
            database.activityDao().deleteAll()
            database.gpsDao().deleteAll()
            database.proximityDao().deleteAll()
            //database.personDao().insertAll(peopleList)
            //Post
            //jsonReader = JsonReader(inputPostStream.reader())
            //val postList: List<Post> = gsonBuilder.fromJson(jsonReader, postType)
            database.postDao().deleteAll()
            //database.postDao().insertAll(postList)
            //Award
            //jsonReader = JsonReader(inputAwardStream.reader())
            //val awardList: List<Award> = gsonBuilder.fromJson(jsonReader, awardType)
            database.awardDao().deleteAll()
            //database.awardDao().insertAll(awardList)
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        } finally {
            jsonReader?.close()
        }
    }
}
