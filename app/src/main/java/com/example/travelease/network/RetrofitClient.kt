package com.example.travelease.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    private const val BASE_URL = " https://travel-ease-api-dpbkcyd3a8cse3gg.mexicocentral-01.azurewebsites.net/"

    val apiService: TravelEaseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convierte el JSON a Kotlin
            .build()
            .create(TravelEaseApi::class.java)
    }

}