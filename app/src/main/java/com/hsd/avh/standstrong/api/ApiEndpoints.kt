package com.hsd.avh.standstrong.api


import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.messages.Message
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import com.hsd.avh.standstrong.data.posts.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiEndpoints{
    @GET("/api/mothers?")
    fun getMotherAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiPerson>>>

    @GET("/api/mothers/{id}")
    fun getMotherByIdAsync(@Path("id") id:Int): Deferred<Response<ApiPerson>>


    @GET("/api/awards?")
    fun getAwardsAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiAward>>>  // e.g id>2


    @GET("/api/gpss?")
    fun getGPSDataAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiGPS>>>

    @GET("/api/mothers/proximity-charts?")
    fun getProximityDataAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiProximity>>>

    @GET("/api/activities?")
    fun getActivityDataAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiActivity>>>

    @POST("/api/posts")
    @FormUrlEncoded
    fun postMessages(@Body msg: Message): Call<Message>

    @GET("/api/posts")
    fun retrieveMessages(): Call<List<Message>>


}