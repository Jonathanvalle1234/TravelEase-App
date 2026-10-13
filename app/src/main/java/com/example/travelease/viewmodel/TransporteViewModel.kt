package com.example.travelease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelease.model.*
import com.example.travelease.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransporteViewModel : ViewModel() {

    // Estado para la lista de transportes de la tarjeta
    private val _transportes = MutableStateFlow<List<Transporte>>(emptyList())
    val transportes: StateFlow<List<Transporte>> = _transportes

    // Estado para llenar el Dropdown dinámicamente
    private val _tiposTransporte = MutableStateFlow<List<TipoTransporte>>(emptyList())
    val tiposTransporte: StateFlow<List<TipoTransporte>> = _tiposTransporte

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Al iniciar, cargamos el catálogo para que el Dropdown funcione de inmediato
        cargarTiposTransporte()
    }

    fun cargarTiposTransporte() {
        viewModelScope.launch {
            try {
                val token = ClienteViewModel.authToken
                val response = RetrofitClient.apiService.obtenerTiposTransporte(token)
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    _tiposTransporte.value = lista
                    // 🚀 RASTREADOR DE ÉXITO
                    println("✅ CARGA EXITOSA: Se descargaron ${lista.size} tipos de transporte desde Django.")
                } else {
                    // 🚀 RASTREADOR DE ERROR HTTP (Ej: 404, 401)
                    println("❌ ERROR DEL SERVIDOR (Tipos Transporte): Código ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // 🚀 RASTREADOR DE CRASHEO (Ej: No hay internet o error leyendo el JSON)
                println("❌ ERROR FATAL DE CONEXIÓN (Tipos Transporte): ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun cargarTransportes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = ClienteViewModel.authToken
                val response = RetrofitClient.apiService.obtenerTransportes(token)
                if (response.isSuccessful) {
                    // Filtramos solo los activos, tal como lo hace el backend
                    _transportes.value = response.body()?.filter { it.active } ?: emptyList()
                }
            } catch (e: Exception) { e.printStackTrace() }
            finally { _isLoading.value = false }
        }
    }

    fun guardarTransporte(
        modo: String,
        transporteId: Int,
        tipoId: Int, // ID del tipo elegido en el Dropdown
        origen: String,
        destino: String,
        precioStr: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val precio = precioStr.replace(",", ".").trim().toDoubleOrNull() ?: 0.0
                val token = ClienteViewModel.authToken

                val isSuccess = if (modo == "crear") {
                    // CREAR: Se manda todo de un solo golpe
                    val req = TransporteRequest(tipoId, origen, destino, precio)
                    RetrofitClient.apiService.crearTransporte(token, req).isSuccessful
                } else {
                    // EDITAR: La técnica del doble endpoint
                    // 1. Mandamos el PATCH sin precio
                    val reqUpdate = TransporteUpdateDto(tipoId, origen, destino)
                    val respPatch = RetrofitClient.apiService.actualizarTransporte(token, transporteId, reqUpdate)

                    if (respPatch.isSuccessful) {
                        // 2. Mandamos el POST del precio inmediatamente después
                        val respPrice = RetrofitClient.apiService.actualizarPrecioTransporte(
                            token, transporteId, mapOf("price" to precio)
                        )
                        respPrice.isSuccessful
                    } else { false }
                }

                if (isSuccess) {
                    cargarTransportes() // Refrescamos la lista
                    onSuccess()
                } else {
                    onError("Error al guardar en el servidor")
                }
            } catch (e: Exception) {
                onError("Fallo de red: ${e.message}")
            }
        }
    }

    fun eliminarTransporte(id: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val token = ClienteViewModel.authToken
                val response = RetrofitClient.apiService.eliminarTransporte(token, id)
                if (response.isSuccessful) {
                    cargarTransportes() // Refresca la lista al borrar
                    onSuccess()
                } else {
                    onError("No se pudo eliminar")
                }
            } catch (e: Exception) { onError(e.message ?: "Error") }
        }
    }
}