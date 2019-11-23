package com.walnut.googleapi.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val okhttp = OkHttpClient()

object PhotosClient {

    private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(PhotosAPI.BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}

object TokenRetrofitClient {

    private val retrofit = Retrofit.Builder()
            .baseUrl(TokenPAI.BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}