package com.example.travelease.repository

import com.example.travelease.model.PowerBiEmbedResponse
import com.example.travelease.network.ReportsRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReportRepository {
    suspend fun obtenerReporte(key: String): Result<PowerBiEmbedResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ReportsRetrofitClient.apiService.obtenerReporteEmbed(key)
            if (response.isSuccessful) {
                val valor = response.isSuccessful()
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(IllegalStateException("La API devolvió una respuesta vacía."))
            } else {
                Result.failure(IllegalStateException("Error ${response.code()} al cargar el reporte."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
