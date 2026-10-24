package com.example.travelease.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Se mantiene la URL de producción (Azure) para que Aurelio pueda probarlo.
    // (Se corrigió un espacio en blanco al inicio que causaba error).
    private const val BASE_URL = "https://travel-ease-api-dpbkcyd3a8cse3gg.mexicocentral-01.azurewebsites.net/"

    // Dejo tu IP local comentada por si necesitas volver a probar en tu red local más adelante
    // private const val BASE_URL = "http://172.25.50.12:8000/"

    // Creamos el interceptor para registrar peticiones y respuestas en el Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Asociamos el interceptor al cliente HTTP de OkHttp
    private val okHttpClient = OkHttpClient.Builder()
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