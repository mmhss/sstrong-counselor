package com.hsd.avh.standstrong.viewmodels

import androidx.collection.ArrayMap
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.data.posts.Post
import com.hsd.avh.standstrong.data.posts.PostRepository
import java.text.SimpleDateFormat
import java.util.*
import androidx.sqlite.db.SimpleSQLiteQuery




class PostListViewModel internal constructor(
        private val postRepository: PostRepository
) : ViewModel() {

    private val postFilterList = MutableLiveData<Int>()
    private lateinit var appliedFilters : ArrayMap<String, List<String>>

    //amount of items per page while paging
    private val PAGE_SIZE = 20

    init {
        postFilterList.value = NO_FILTER
    }

    private val posts: LiveData<PagedList<Post>> = Transformations.switchMap(postFilterList) {

        if (StandStrong.isNotRA()) {

            var sDate: Long =-1
            var eDate: Long =-1

            if (it == NO_FILTER) {

                return@switchMap LivePagedListBuilder<Int, Post>(postRepository.getAllPaged(), PAGE_SIZE).build()
            } else {

                var fl: ArrayList<Int> = ArrayList<Int>()
                var q: String = "SELECT * FROM posts "
                var hasFilters: Boolean = false
                for ((k, v) in appliedFilters) {
                    if (k == "date") {
                        var stringDate = appliedFilters["date"]!![0].toString()
                        val format = SimpleDateFormat("yyyy-MM-dd")
                        try {
                            val date = format.parse(stringDate)
                            sDate = setTime(date.time, 0, 0, 0)
                            eDate = setTime(date.time, 23, 59, 59)
                            System.out.println(date)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    if (k != "date") {
                        hasFilters = true
                        q = "$q WHERE type IN ("
                        v.forEach { filterItem ->
                            when (filterItem) {
                                StandStrong.POST_CARD_STRING_MESSAGE -> q = q + StandStrong.POST_CARD_MESSAGE + ","
                                StandStrong.POST_CARD_STRING_PROXIMITY -> q = q + StandStrong.POST_CARD_PROXIMITY + ","
                                StandStrong.POST_CARD_STRING_GPS -> q = q + StandStrong.POST_CARD_GPS + ","
                                StandStrong.POST_CARD_STRING_ACTIVITY -> q = q + StandStrong.POST_CARD_ACTIVITY + ","
                                StandStrong.POST_CARD_STRING_AWARD -> q = q + StandStrong.POST_CARD_AWARD + ","
                                StandStrong.POST_CARD_STRING_CONTENT -> q = q + StandStrong.POST_CARD_CONTENT + ","
                                StandStrong.POST_CARD_STRING_GOAL -> q = q + StandStrong.POST_CARD_GOAL + ","
                            }
                        }
                        if (q.substring(q.length - 1) == ",") {
                            q = q.substring(0, q.length - 1);
                            q = "$q)"
                        }
                    }
                }
                q = if (sDate > 0) {
                    if (hasFilters) {
                        "$q AND date BETWEEN $sDate AND $eDate ORDER BY date desc"
                    } else {
                        "$q WHERE date BETWEEN $sDate AND $eDate ORDER BY date desc"
                    }

                } else {
                    "$q ORDER BY date desc"
                }

                val query = SimpleSQLiteQuery(q)

                return@switchMap LivePagedListBuilder<Int, Post>(postRepository.getFilteredPagedPosts(query), PAGE_SIZE).build()
            }
        } else
            return@switchMap LivePagedListBuilder<Int, Post>(postRepository.getRAPostsPaged(), PAGE_SIZE).build()
    }

    fun getPostsPaged() = posts

    fun setFilter(filter: ArrayMap<String, List<String>> ) {
        //ArrayMap<String, List<String>> applied_filters = new ArrayMap<>();
        appliedFilters = filter

        postFilterList.value = Random().nextInt(20000 )
    }

    fun clearFilter() {
        postFilterList.value = NO_FILTER
    }

    fun isFiltered() = postFilterList.value != NO_FILTER

    fun updatePosts() {
        postRepository.refreshPostList()
    }

    fun updatePostsWithFilter(filters: androidx.collection.ArrayMap<String, List<String>>) {
        postRepository.refreshPostList()
    }

    private fun setTime(dateInMill: Long,h:Int,m:Int,s:Int): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = dateInMill
        cal.set(Calendar.HOUR_OF_DAY, h)
        cal.set(Calendar.MINUTE, m)
        cal.set(Calendar.SECOND, s)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    companion object {
        private const val NO_FILTER = -1
    }
}
