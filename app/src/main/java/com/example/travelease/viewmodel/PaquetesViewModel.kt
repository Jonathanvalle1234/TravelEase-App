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

    var hayMasPaginas by mutableStateOf(false)
        private set

    private var paginaActual = 1


    fun obtenerPaquetes(token: String, pagina: Int = 1, resetear: Boolean = true) {
        if (isLoading) return
        viewModelScope.launch {
            isLoading = true
            try {
                val authToken = if (token.startsWith("Token ")) token else "Token $token"
                val response = RetrofitClient.apiService.obtenerPaquetes(authToken, pagina)

                if (response.isSuccessful) {
                    val body = response.body()
                    val nuevos = body?.data ?: emptyList()
                    paquetesList = if (resetear) nuevos else paquetesList + nuevos
                    paginaActual = body?.pagination?.currentPage ?: 1
                    hayMasPaginas = body?.pagination?.hasNextPage ?: false
                    Log.d("API_PAQUETES", "Página $paginaActual: ${nuevos.size} paquetes. ¿Más? $hayMasPaginas")
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


    fun cargarMasPaquetes(token: String) {
        if (hayMasPaginas && !isLoading) {
            obtenerPaquetes(token, paginaActual + 1, resetear = false)
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