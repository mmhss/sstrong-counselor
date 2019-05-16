package com.hsd.avh.standstrong.api

import android.preference.PreferenceManager
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.utilities.Const
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiService {

    private var endpoints: ApiEndpoints? = null

    val service: ApiEndpoints?
        get() {
            if (endpoints == null) {

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.HEADERS

                val client = OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor {chain ->

                            //adding token to each request if its exist
                            val newRequest = chain.request()
                                    .newBuilder()
                                    .addHeader("Authorization", "Bearer ${PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext()).getString(Const.ARG_TOKEN, "")}")
                                    .build()

                            chain.proceed(newRequest)
                        }
                        .build()

                val retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .client(client)
                        .baseUrl("https://standstrong.herokuapp.com")
                        .build()

                endpoints = retrofit.create(ApiEndpoints::class.java)
            }

            return endpoints

        }

}