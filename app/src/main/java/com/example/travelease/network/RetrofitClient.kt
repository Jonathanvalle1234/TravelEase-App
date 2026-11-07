package com.example.travelease.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://travel-ease-api-dpbkcyd3a8cse3gg.mexicocentral-01.azurewebsites.net/"


    // Dejo tu IP local comentada por si necesitas volver a probar en tu red local más adelante
    //private const val BASE_URL = "http://192.168.1.41:8000/"

    // Creamos el interceptor para registrar peticiones y respuestas en el Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Asociamos el interceptor y ampliamos los timeouts a 30s para soportar el cold start de Azure
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: TravelEaseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TravelEaseApi::class.java)
    }
}