package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class PowerBiEmbedResponse(
    @SerializedName("reportId") val reportId: String,
    @SerializedName("datasetId") val datasetId: String?,
    @SerializedName("embedUrl") val embedUrl: String,
    @SerializedName("embedToken") val embedToken: String,
    @SerializedName("expiration") val expiration: String?,
    @SerializedName("pageName") val pageName: String?,
    @SerializedName("visualName") val visualName: String?
)
