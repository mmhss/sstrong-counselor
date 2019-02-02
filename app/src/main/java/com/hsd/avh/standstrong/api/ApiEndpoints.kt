package com.hsd.avh.standstrong.api

import com.hsd.avh.standstrong.data.awards.ApiAward
import com.hsd.avh.standstrong.data.people.ApiPerson
import com.hsd.avh.standstrong.data.people.Person
import retrofit2.Call
import retrofit2.http.GET


interface ApiEndpoints{
    @GET("/api/mothers")
    fun getMother(): Call<List<ApiPerson>>

    //TODO AWARDS API
    @GET("/api/mothers")
    fun getAwards(): Call<List<ApiAward>>

}