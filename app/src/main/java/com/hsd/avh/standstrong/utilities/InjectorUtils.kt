package com.hsd.avh.standstrong.utilities

import android.content.Context

import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.data.awards.AwardRepository
import com.hsd.avh.standstrong.data.awards.MessageRepository
import com.hsd.avh.standstrong.data.people.PersonRepository
import com.hsd.avh.standstrong.data.posts.PostRepository
import com.hsd.avh.standstrong.viewmodels.*

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getPostRepository(context: Context): PostRepository {
        return PostRepository.getInstance(AppDatabase.getInstance(context).postDao(),AppDatabase.getInstance(context).gpsDao(),AppDatabase.getInstance(context).activityDao(),AppDatabase.getInstance(context).proximityDao())
    }
    private fun getAwardRepository(context: Context): AwardRepository {
        return AwardRepository.getInstance(AppDatabase.getInstance(context).awardDao())
    }
    private fun getPeopleRepository(context: Context): PersonRepository {
        return PersonRepository.getInstance(AppDatabase.getInstance(context).personDao(),AppDatabase.getInstance(context).postDao()
                ,AppDatabase.getInstance(context).awardDao(),AppDatabase.getInstance(context).messageDao())
    }

    private fun getMessageRepository(context: Context): MessageRepository {
        return MessageRepository.getInstance(AppDatabase.getInstance(context).messageDao())
    }

    private fun getPostDataRepository(context: Context): PostRepository {
        return PostRepository.getInstance(AppDatabase.getInstance(context).postDao(),AppDatabase.getInstance(context).gpsDao(),AppDatabase.getInstance(context).activityDao(),AppDatabase.getInstance(context).proximityDao())
    }

    fun providePostDataViewModelFactory(
            context: Context,
            motherId: Int,
            postDate: Long
    ): DataPostViewModelFactory {
        val repository = getPostDataRepository(context)
        return DataPostViewModelFactory(repository,motherId,postDate)
    }


    fun providePostListViewModelFactory(
        context: Context
    ): PostListViewModelFactory {
        val repository = getPostRepository(context)
        return PostListViewModelFactory(repository)
    }

    fun provideAwardListViewModelFactory(
            context: Context
    ): AwardViewModelFactory {
        val repository = getAwardRepository(context)
        return AwardViewModelFactory(repository)
    }

    fun providePersonViewModelFactory(
            context: Context
    ): PeopleViewModelFactory {
        val repository = getPeopleRepository(context)
        return PeopleViewModelFactory(repository)
    }

    fun providePersonDetailViewModelFactory(
        context: Context,
        personId: String
    ): PersonDetailViewModelFactory {
        return PersonDetailViewModelFactory(getPeopleRepository(context), personId)
    }

    fun providePostDetailViewModelFactory(
            context: Context,
            postId: Int
    ): PostDetailViewModelFactory {
        return PostDetailViewModelFactory(getPostRepository(context), postId)
    }


    fun provideMessageViewModelFactory(
            context: Context,
            motherId: Int,
            postId: Int
    ): MessageViewModelFactory {
        return MessageViewModelFactory(getMessageRepository(context), motherId, postId)
    }

    fun getResourceID(resName: String, resType: String, ctx: Context): Int {
        val ResourceID = ctx.resources.getIdentifier(resName, resType,
                ctx.applicationInfo.packageName)
        return if (ResourceID == 0) {
            throw IllegalArgumentException(
                    "No resource string found with name $resName"
            )
        } else {
            ResourceID
        }
    }
}
