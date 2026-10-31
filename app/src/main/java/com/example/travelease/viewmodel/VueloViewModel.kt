package com.example.travelease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelease.model.Vuelo
import com.example.travelease.model.VueloRequestDto
import com.example.travelease.model.VueloUpdateDto
import com.example.travelease.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VueloViewModel : ViewModel() {

    private val _vuelos = MutableStateFlow<List<Vuelo>>(emptyList())
    val vuelos: StateFlow<List<Vuelo>> = _vuelos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun cargarVuelos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tokenActual = ClienteViewModel.authToken
                val response = RetrofitClient.apiService.obtenerVuelos(tokenActual)
                if (response.isSuccessful) {
                    _vuelos.value = response.body()?.filter { it.active } ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obtenerVueloPorId(id: Int, onVueloLoaded: (Vuelo) -> Unit) {
        viewModelScope.launch {
            try {
                val tokenActual = ClienteViewModel.authToken
                val response = RetrofitClient.apiService.obtenerVuelos(tokenActual)
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    lista.find { it.id == id }?.let { onVueloLoaded(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun guardarVuelo(
        modo: String,
        vueloId: Int,
        aerolinea: String,
        precioStr: String,
        origen: String,
        destino: String,
        fechaSalida: String,
        fechaEntrada: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val fSalida = formatearFechaParaDjango(fechaSalida)
                val fEntrada = formatearFechaParaDjango(fechaEntrada)
                val precioLimpio = precioStr.replace(",", ".").trim()
                val precio = precioLimpio.toDoubleOrNull() ?: 0.0
                val tokenActual = ClienteViewModel.authToken

                val isSuccess = if (modo == "crear") {
                    val requestCrear = VueloRequestDto(
                        aerolinea = aerolinea,
                        origen = origen,
                        destino = destino,
                        fechaSalida = fSalida,
                        fechaEntrada = fEntrada,
                        precio = precio
                    )
                    RetrofitClient.apiService.crearVuelo(tokenActual, requestCrear).isSuccessful
                } else {
                    val requestEditar = VueloUpdateDto(
                        aerolinea = aerolinea,
                        origen = origen,
                        destino = destino,
                        fechaSalida = fSalida,
                        fechaEntrada = fEntrada
                    )

                    val responsePut = RetrofitClient.apiService.actualizarVueloParcial(tokenActual, vueloId, requestEditar)

                    if (responsePut.isSuccessful) {
                        val respPrecio = RetrofitClient.apiService.actualizarPrecioVuelo(
                            token = tokenActual,
                            id = vueloId,
                            priceMap = mapOf("price" to precio)
                        )
                        respPrecio.isSuccessful
                    } else {
                        val errorBody = responsePut.errorBody()?.string() ?: "Sin detalles"
                        onError(errorBody)
                        false
                    }
                }

                if (isSuccess) {
                    cargarVuelos()
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError("Fallo de red: ${e.message}")
            }
        }
    }

    //  FUNCIÓN PARA ELIMINAR UN VUELO DESDE EL VIEWMODEL
    fun eliminarVuelo(
        vueloId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val tokenActual = ClienteViewModel.authToken
                println("===  ELIMINANDO VUELO $vueloId EN DJANGO ===")

                val response = RetrofitClient.apiService.eliminarVuelo(tokenActual, vueloId)

                if (response.isSuccessful) {
                    println(" ¡Éxito! Vuelo eliminado correctamente de Django.")
                    cargarVuelos() // Refrescamos la lista de vuelos automáticamente
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                    println(" Error al eliminar vuelo: $errorBody")
                    onError(errorBody)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError("Fallo de red al eliminar: ${e.message}")
            }
        }
    }

    private fun formatearFechaParaDjango(fecha: String): String {
        return try {
            val partes = fecha.split("/")
            if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}" else fecha
        } catch (e: Exception) {
            fecha
        }
    }
}