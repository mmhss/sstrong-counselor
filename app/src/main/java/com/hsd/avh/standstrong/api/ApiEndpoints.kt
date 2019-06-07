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

    @POST("/api/posts")
    fun sendMessageToServer(@Body msg: ApiMessage): Call<Message>

    @POST("/api/auth/signin")
    fun login(@Body loginBody: LoginBody) : Call<SignInResponse>

    @GET("/api/mothers?")
    fun getPeopleAsync(@Query("search")lastRow: String) : Call<List<ApiPerson>>

    //get data
    @GET("/api/posts/find")
    fun getMessagesAsync(@Query("search") searchId: String, @Query("offset") offset: Int = 0, @Query("limit") limit: Int): Call<List<ApiMessage>>

    @GET("/api/awards/find")
    fun getAwardsList(@Query("search") searchId: String, @Query("offset") offset: Int = 0, @Query("limit") limit: Int): Call<List<ApiAward>>

    @GET("/api/activities/find")
    fun getActivityDataAsync(@Query("search") searchId: String, @Query("offset") offset: Int = 0, @Query("limit") limit: Int): Deferred<Response<List<ApiActivity>>>

    @GET("/api/gpss/find")
    fun getGPSDataAsync(@Query("search") searchId: String, @Query("offset") offset: Int = 0, @Query("limit") limit: Int): Deferred<Response<List<ApiGPS>>>

    @GET("/api/proximities/proximity-charts/{proximitySyncId}/{limit}")
    fun getProximityDataAsync(@Path("proximitySyncId") searchId:Int, @Path("limit") limit: Int): Deferred<Response<List<ApiProximity>>>
}