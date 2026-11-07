package com.example.travelease.model

import com.google.gson.annotations.SerializedName

data class PaginacionDto(
    @SerializedName("total_records") val totalRecords: Int = 0,
    @SerializedName("per_page") val perPage: Int = 20,
    @SerializedName("current_page") val currentPage: Int = 1,
    @SerializedName("total_pages") val totalPages: Int = 1,
    @SerializedName("has_next_page") val hasNextPage: Boolean = false,
    @SerializedName("has_prev_page") val hasPrevPage: Boolean = false
)

data class RespuestaPaginadaDto<T>(
    @SerializedName("data") val data: List<T> = emptyList(),
    @SerializedName("pagination") val pagination: PaginacionDto? = null
)