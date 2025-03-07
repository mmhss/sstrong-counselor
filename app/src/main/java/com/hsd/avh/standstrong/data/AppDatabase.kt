package com.hsd.avh.standstrong.data

import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.background.systemalarm.ConstraintProxyUpdateReceiver
import com.hsd.avh.standstrong.data.awards.Award
import com.hsd.avh.standstrong.data.awards.AwardDao
import com.hsd.avh.standstrong.data.awards.MessageDao
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.people.PersonDao
import com.hsd.avh.standstrong.data.posts.*
import com.hsd.avh.standstrong.utilities.DATABASE_NAME
import com.hsd.avh.standstrong.workers.SeedDatabaseWorker

/**
 * The Room database for this app
 */
@Database(entities = [Person::class, Post::class, Award::class, Message::class,Activity::class,Proximity::class,Gps::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun postDao(): PostDao
    abstract fun awardDao(): AwardDao
    abstract fun messageDao(): MessageDao
    abstract fun activityDao(): ActivityDao
    abstract fun proximityDao(): ProximityDao
    abstract fun gpsDao(): GPSDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance().enqueue(request)
                        }
                    })
                    .build()
        }
    }

}
