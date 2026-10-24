package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class CatalogoItemDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName(
        value = "name",
        alternate = ["nombre", "title", "titulo", "full_name", "nombre_completo", "description"]
    )
    val nombre: String? = null
)