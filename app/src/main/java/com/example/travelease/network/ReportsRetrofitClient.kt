package com.example.travelease.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** 10.0.2.2 permite al emulador Android acceder al localhost de la computadora. */
object ReportsRetrofitClient {
    private const val BASE_URL = "http://192.168.1.41:8000/"

    val apiService: TravelEaseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TravelEaseApi::class.java)
    }
}
