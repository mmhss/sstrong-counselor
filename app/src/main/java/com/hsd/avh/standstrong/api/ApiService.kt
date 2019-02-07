package com.hsd.avh.standstrong.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


object ApiService {

    private var endpoints: ApiEndpoints? = null

    val service: ApiEndpoints?
        get() {
            if (endpoints == null) {
                val retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .baseUrl("http://restapi-dev2.ap-southeast-1.elasticbeanstalk.com/")
                        .build()

                endpoints = retrofit.create(ApiEndpoints::class.java)
            }

            return endpoints

        }

}