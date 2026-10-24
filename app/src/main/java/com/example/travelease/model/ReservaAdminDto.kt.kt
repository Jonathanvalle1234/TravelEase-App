package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class CurrentStatusDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String? = null,
    @SerializedName("code") val code: String? = null
)

data class ReservaAdminDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("client")
    val clienteId: Int? = null,

    @SerializedName("client_name")
    val clienteNombre: String? = null,

    @SerializedName("package_reserved_id")
    val paqueteId: Int? = null,

    @SerializedName("package_name")
    val paqueteNombre: String? = null,

    @SerializedName("payment_type")
    val tipoPagoId: Int? = null,

    @SerializedName("payment_type_name")
    val tipoPago: String? = null,

    @SerializedName("seller_user")
    val sellerUserId: Int? = null,

    @SerializedName("total")
    val total: String? = null,

    @SerializedName("current_status")
    val currentStatus: CurrentStatusDto? = null
) {
    val nombreCliente: String
        get() = clienteNombre?.takeIf { it.isNotBlank() } ?: "Cliente #${clienteId ?: id}"

    val tituloPaquete: String
        get() = paqueteNombre?.takeIf { it.isNotBlank() } ?: "Paquete general"

    val precio: Double
        get() = total?.toDoubleOrNull() ?: 0.0

    val estado: String?
        get() = currentStatus?.name
}