package com.example.travelease.repository

import com.example.travelease.model.EncuestaDto
import com.example.travelease.model.ValoracionDto
import com.example.travelease.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClienteRepository {


    suspend fun enviarEncuesta(encuesta: EncuestaDto): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                true // Por ahora, simulamos que el envío fue un éxito
            } catch (e: Exception) {
                false
            }
        }
    }


    suspend fun enviarValoracion(token: String, valoracion: ValoracionDto): Boolean { // <-- Agregamos el token aquí
        return withContext(Dispatchers.IO) {
            try {
                // Pasamos el token como primer parámetro
                val respuesta = RetrofitClient.apiService.enviarValoracionBackend(token, valoracion)

                if (respuesta.isSuccessful) {
                    println(" DJANGO DICE: GUARDADO PERFECTO (Código ${respuesta.code()})")
                    true
                } else {
                    println(" DJANGO RECHAZÓ EL DATO. Código de error: ${respuesta.code()}")
                    println(" MOTIVO EXACTO: ${respuesta.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                println(" ERROR DE RED O CAÍDA: ${e.message}")
                e.printStackTrace()
                false
            }
        }


    }
    suspend fun enviarSugerencia(token: String, sugerencia: com.example.travelease.model.SugerenciaDto): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = RetrofitClient.apiService.enviarSugerencia(token, sugerencia)
                if (respuesta.isSuccessful) {
                    println(" DJANGO DICE: SUGERENCIA GUARDADA PERFECTA (Código ${respuesta.code()})")
                    true
                } else {
                    println(" DJANGO RECHAZÓ LA SUGERENCIA. Código de error: ${respuesta.code()}")
                    println(" MOTIVO EXACTO: ${respuesta.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                println(" ERROR DE RED: ${e.message}")
                e.printStackTrace()
                false
            }
        }
    }
}
