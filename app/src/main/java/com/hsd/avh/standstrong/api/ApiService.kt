package com.hsd.avh.standstrong.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object ApiService {

    private var endpoints: ApiEndpoints? = null

    val service: ApiEndpoints?
        get() {
            if (endpoints == null) {

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

                val retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .client(client)
                        .baseUrl("http://restapi-dev2.ap-southeast-1.elasticbeanstalk.com/")
                        .build()

                endpoints = retrofit.create(ApiEndpoints::class.java)
            }

            return endpoints

        }

}