package com.hsd.avh.standstrong.api


import com.hsd.avh.standstrong.data.LoginBody
import com.hsd.avh.standstrong.data.SignInResponse
import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.messages.ApiMessage
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

    @GET("/api/awards")
    fun getAwardsList(@Query("search") searchId: String): Call<List<ApiAward>>

    @GET("/api/gpss?")
    fun getGPSDataAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiGPS>>>

    @GET("/api/proximities/proximity-charts/{proximitySyncId}")
    fun getProximityDataAsync(@Path("proximitySyncId") searchId:Int): Deferred<Response<List<ApiProximity>>>

    @GET("/api/activities?")
    fun getActivityDataAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiActivity>>>

    @POST("/api/posts")
    @FormUrlEncoded
    fun postMessages(@Body msg: Message): Call<Message>

    @POST("/api/posts")
    fun sendMessageToServer(@Body msg: ApiMessage): Call<Message>

    @GET("/api/posts?")
    fun getMessagesAsync(@Query("search") searchId:String ): Deferred<Response<List<ApiMessage>>>

    @POST("/api/auth/signin")
    fun login(@Body loginBody: LoginBody) : Call<SignInResponse>


}