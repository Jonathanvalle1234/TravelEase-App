package com.example.travelease.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelease.model.DestinationRequestDto
import com.example.travelease.model.PaqueteDto
import com.example.travelease.model.PaqueteRequestDto
import com.example.travelease.model.ServicioDetalleDto
import com.example.travelease.network.RetrofitClient
import kotlinx.coroutines.launch

class PaquetesViewModel : ViewModel() {

    var paquetesList by mutableStateOf<List<PaqueteDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Paquetes no está paginado, siempre false
    var hayMasPaginas by mutableStateOf(false)
        private set

    // Carga todos los paquetes (lista simple, sin paginación)
    fun obtenerPaquetes(token: String) {
        if (isLoading) return
        viewModelScope.launch {
            isLoading = true
            try {
                val authToken = if (token.startsWith("Token ")) token else "Token $token"
                val response = RetrofitClient.apiService.obtenerPaquetes(authToken)

                if (response.isSuccessful) {
                    paquetesList = response.body() ?: emptyList()
                    hayMasPaginas = false
                    Log.d("API_PAQUETES", "Paquetes cargados: ${paquetesList.size}")
                } else {
                    Log.e("API_PAQUETES", "Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_PAQUETES", "Fallo al cargar paquetes: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun guardarPaquete(
        modo: String,
        paqueteId: Int,
        titulo: String,
        descripcion: String,
        pais: String,
        ciudad: String,
        vueloId: Int?,
        hotelId: Int?,
        transporteId: Int?,
        token: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val authToken = if (token.startsWith("Token ")) token else "Token $token"

                val detalles = listOfNotNull(
                    vueloId?.let { ServicioDetalleDto("Vuelo", it, 1) },
                    hotelId?.let { ServicioDetalleDto("Hotel", it, 1) },
                    transporteId?.let { ServicioDetalleDto("Transporte", it, 1) }
                )

                val request = PaqueteRequestDto(
                    titulo = titulo,
                    descripcion = descripcion,
                    destination = DestinationRequestDto(city = ciudad, country = pais),
                    details = detalles
                )

                Log.d("DEBUG_PAQUETE", "enviando: ${com.google.gson.Gson().toJson(request)}")

                val response = if (modo == "crear") {
                    RetrofitClient.apiService.crearPaquete(authToken, request)
                } else {
                    RetrofitClient.apiService.actualizarPaquete(authToken, paqueteId, request)
                }

                if (response.isSuccessful) {
                    obtenerPaquetes(authToken)
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("DJANGO_ERROR", "Respuesta de error: $errorBody")
                    onError(errorBody)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError("Fallo de red: ${e.message}")
            }
        }
    }
}