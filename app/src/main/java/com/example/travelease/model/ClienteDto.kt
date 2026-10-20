package com.example.travelease.model

import com.google.gson.annotations.SerializedName

// 1. EL MODELO PARA LEER (GET)
data class ClienteDto(
    val id: Int = 0,
    val siglas: String = "",
    val nombre: String = "",
    val montoTotal: Double = 0.0,
    val rank: String = "",

    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("user_email") val userEmail: String = "",
    val municipality: Int = 0,
    @SerializedName("municipality_name") val municipalityName: String = "",
    @SerializedName("department_id") val departmentId: Int = 0,

    val name: String = "",
    @SerializedName("national_id") val nationalId: String = "",
    val address: String = "",
    @SerializedName("phone_number") val phoneNumber: String = "",
    val active: Boolean = true
)

// 2. EL MODELO PARA ESCRIBIR (POST - Exacto al Body de Bruno)
data class ClienteRequestDto(
    val name: String,
    @SerializedName("email") val userEmail: String?,       // <-- Cambiado de "user_email" a "email"
    @SerializedName("national_id") val nationalId: String,
    val address: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val municipality: Int
    // department_id eliminado porque el POST de Django no lo pide en el cuerpo
)

data class ClienteActualizarRequestDto(
    val name: String,
    @SerializedName("national_id") val nationalId: String,
    val address: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val municipality: Int
    // department_id eliminado porque el POST de Django no lo pide en el cuerpo
)