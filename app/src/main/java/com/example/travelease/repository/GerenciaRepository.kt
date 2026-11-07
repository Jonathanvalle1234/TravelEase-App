package com.example.travelease.repository

import com.example.travelease.model.ClienteDto
import com.example.travelease.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GerenciaRepository {


    suspend fun obtenerTopClientes(): List<ClienteDto> {
        return withContext(Dispatchers.IO) {
            try {

                // RetrofitClient.apiService.getTopClientes()

                emptyList()
            } catch (e: Exception) {
                // Si el servidor de Django se cae o no hay internet, devolvemos vacío
                emptyList()
            }
        }
    }
}