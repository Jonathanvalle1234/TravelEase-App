package com.example.travelease.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelease.model.Hotel
import com.example.travelease.model.HotelRequest
import com.example.travelease.model.PrecioRequest
import com.example.travelease.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HotelViewModel : ViewModel() {

    private val _hoteles = MutableStateFlow<List<Hotel>>(emptyList())
    val hoteles: StateFlow<List<Hotel>> = _hoteles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private fun getTokenSeguro(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
        val tokenGuardado = sharedPreferences.getString("TOKEN", "") ?: ""
        return "Token $tokenGuardado"
    }

    fun obtenerHoteles(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tokenFinal = getTokenSeguro(context)
                val response = RetrofitClient.apiService.obtenerHoteles(tokenFinal)
                if (response.isSuccessful) {
                    _hoteles.value = response.body() ?: emptyList()
                    println("✅ ¡Hoteles descargados con éxito!")
                } else {
                    println("❌ Error Django: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarHotel(context: Context, id: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.eliminarHotel(getTokenSeguro(context), id)
                if (response.isSuccessful) {
                    obtenerHoteles(context)
                    onSuccess()
                } else {
                    onError("Error al eliminar")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error de red")
            }
        }
    }

    fun guardarHotel(context: Context, hotelId: Int?, nombre: String, ciudad: String, estrellas: Int, precioStr: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val precio = precioStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                val token = getTokenSeguro(context)

                if (hotelId == null || hotelId == 0) {
                    // 🔥 CREAR: 1 sola API con todo (Usa HotelRequest)
                    val request = HotelRequest(nombre, ciudad, estrellas, precio)
                    val response = RetrofitClient.apiService.crearHotel(token, request)

                    if (response.isSuccessful) {
                        obtenerHoteles(context)
                        onSuccess()
                    } else {
                        onError("Error al crear: ${response.errorBody()?.string()}")
                    }
                } else {
                    // 🔥 EDITAR: 2 APIs separadas (Usa HashMap para el PATCH)
                    val datosPatch = java.util.HashMap<String, Any>()
                    datosPatch["name"] = nombre
                    datosPatch["city"] = ciudad
                    datosPatch["stars"] = estrellas

                    // 1. PATCH solo con datos básicos
                    val patchResp = RetrofitClient.apiService.actualizarHotel(token, hotelId, datosPatch)

                    if (patchResp.isSuccessful) {
                        // 2. POST solo para el precio nuevo
                        val priceResp = RetrofitClient.apiService.actualizarPrecioHotel(token, hotelId, PrecioRequest(precio))
                        if (priceResp.isSuccessful) {
                            obtenerHoteles(context)
                            onSuccess()
                        } else {
                            onError("Error al actualizar precio")
                        }
                    } else {
                        onError("Error al actualizar datos del hotel")
                    }
                }
            } catch (e: Exception) {
                onError("Error de conexión: ${e.message}")
            }
        }
    }
}