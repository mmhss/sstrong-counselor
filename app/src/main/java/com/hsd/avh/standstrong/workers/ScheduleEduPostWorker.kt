package com.hsd.avh.standstrong.workers

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.AppDatabase
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.utilities.SSUtils
import com.hsd.avh.standstrong.R
import kotlinx.coroutines.*


class ScheduleEduPostWorker(context : Context, params : WorkerParameters)
    : Worker(context, params) {


    val database = AppDatabase.getInstance(applicationContext)

    override fun doWork(): Result {
        if(StandStrong.getEduPostsShown()<=StandStrong.NUMBER_OF_EDU_POSTS) {
            newEducationPosts()
            setupNextEduPost()
        }

        return Result.success()

    }


    private fun newEducationPosts() {
        val nextPost =SSUtils.getNextEducationalPost()
        val cardTitle = StandStrong.applicationContext().getString(R.string.card_title_education) + " " + StandStrong.getEduPostsShown()
        val mediaUrl = "@drawable/$nextPost"
        val avatarUrl = "https://www.tinygraphs.com/squares/Education?theme=heatwave&numcolors=4&size=50&fmt=png"
        val postTitle = SSUtils.getEducationalPostTitle(nextPost)
        val postTxt =SSUtils.getEducationalPostText(nextPost)
        val p: Post = Post( "All",99999,Date(),avatarUrl,cardTitle,"" ,mediaUrl,false,0,StandStrong.POST_CARD_CONTENT,postTitle,postTxt)

        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){
                database.postDao().insertPost(p)
            }
        }
        //+1 so when worker triggers we are on the next post
        SSUtils.setNextEducationalPost()
        SSUtils.triggerNotification(998, StandStrong.applicationContext().resources.getString(R.string.notification_description_edu))
    }


    private fun setupNextEduPost(){
            val eduWork = OneTimeWorkRequest.Builder(ScheduleEduPostWorker::class.java)
                    .setInitialDelay(StandStrong.EDU_POST_CHECK_FREQUENCY_DAYS.toLong(), TimeUnit.SECONDS)
                    .addTag(StandStrong.TAG_EDU )
                    .build()
            WorkManager.getInstance().enqueueUniqueWork(StandStrong.TAG_EDU, ExistingWorkPolicy.REPLACE ,eduWork)
    }



}