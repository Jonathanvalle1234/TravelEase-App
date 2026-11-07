package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("token")
    val token: String?,

    @SerializedName("username")
    val username: String?,


    @SerializedName("rol")
    val rol: String?
)