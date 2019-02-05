package com.hsd.avh.standstrong.api


import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.posts.ApiActivity
import com.hsd.avh.standstrong.data.posts.ApiGPS
import com.hsd.avh.standstrong.data.posts.ApiProximity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import com.hsd.avh.standstrong.data.posts.Post
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded




interface ApiEndpoints{
    @GET("/api/mothers")
    fun getMother(): Call<List<ApiPerson>>

    @GET("/api/awards")
    fun getAwards(): Call<List<ApiAward>>

    @GET("/api/gpss")
    fun getProximityData(): Call<List<ApiProximity>>

    @GET("/api/proximities/proximitychart")
    fun getGPSData(): Call<List<ApiGPS>>

    //TODO API POST MESSAGE
    @POST("/api/mothers")
    @FormUrlEncoded
    fun postMessages(@Body msg: Message): Call<Message>


    @GET("/api/activities")
    fun getActivityData(): Call<List<ApiActivity>>


    //TODO API GET MESSAGES
    @GET("/api/mothers")
    fun retrieveMessages(): Call<List<Message>>


}