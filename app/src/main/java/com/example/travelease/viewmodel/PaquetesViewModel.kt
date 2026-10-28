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

    // 1. Estados de la interfaz (Variables reactivas)
    var paquetesList by mutableStateOf<List<PaqueteDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    // 2. Función principal para descargar los datos
    fun obtenerPaquetes(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val authToken = if (token.startsWith("Token ")) token else "Token $token"
                val response = RetrofitClient.apiService.obtenerPaquetes(authToken)

                if (response.isSuccessful) {
                    val respuestaBody = response.body()
                    Log.d("JSON_REAL", "Respuesta de Django: $respuestaBody")
                    paquetesList = respuestaBody ?: emptyList()
                    Log.d("API_PAQUETES", "Éxito: Se descargaron ${paquetesList.size} paquetes")
                } else {
                    Log.e("API_PAQUETES", "Error del servidor: Código ${response.code()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_PAQUETES", "Fallo catastrófico al cargar paquetes: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

    // 3. Guardar o actualizar un paquete. Recibe IDs de servicio y arma "details".
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
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido en el servidor"
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