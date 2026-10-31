package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class Hotel(
    val id: Int,
    @SerializedName("name") val nombre: String,
    @SerializedName("city") val ciudad: String,
    @SerializedName("stars") val estrellas: Int,
    @SerializedName("current_price") val precioActual: Double,
    @SerializedName("active") val activo: Boolean = true
)

data class HotelRequest(
    @SerializedName("name") val nombre: String,
    @SerializedName("city") val ciudad: String,
    @SerializedName("stars") val estrellas: Int,
    @SerializedName("price") val precio: Double // O el campo exacto de precio en tu POST
)

// Si el precio se actualiza en el endpoint /prices/ como vi en el video
data class PrecioRequest(
    @SerializedName("price") val precio: Double
)