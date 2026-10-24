package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class ReservaRequest(
    @SerializedName("client_id")
    val clientId: Int,

    @SerializedName("package_id")
    val packageId: Int,

    @SerializedName("payment_type_id")
    val paymentTypeId: Int,

    @SerializedName("status_id")
    val statusId: Int,

    @SerializedName("seller_user_id")
    val sellerUserId: Int = 2 // <--- Valor por defecto obligatorio para que Django no rechace la petición
)