package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class DestinationRequestDto(
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String
)

data class ServicioDetalleDto(
    @SerializedName("service_type") val serviceType: String, // "Vuelo", "Hotel" o "Transporte"
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("quantity") val quantity: Int = 1
)

data class PaqueteRequestDto(
    @SerializedName("name") val titulo: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("destination") val destination: DestinationRequestDto,
    @SerializedName("details") val details: List<ServicioDetalleDto>
)