package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class PaqueteDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName(value = "titulo", alternate = ["title", "nombre_paquete", "nombre"])
    val titulo: String? = "",

    @SerializedName(value = "descripcion", alternate = ["description"])
    val descripcion: String? = "",

    @SerializedName(value = "precio", alternate = ["price"])
    val precio: Double = 0.0
)