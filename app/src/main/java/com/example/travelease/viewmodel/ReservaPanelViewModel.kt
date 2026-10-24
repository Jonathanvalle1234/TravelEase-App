
package com.example.travelease.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelease.model.ReservaAdminDto
import com.example.travelease.model.ReservaRequest
import com.example.travelease.repository.ReservaPanelRepository
import com.example.travelease.view.OpcionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ProtocolException

class ReservaPanelViewModel : ViewModel() {

    private val repository = ReservaPanelRepository()

    private val _reservas = MutableStateFlow<List<ReservaAdminDto>>(emptyList())
    val reservas: StateFlow<List<ReservaAdminDto>> = _reservas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _clientes = MutableStateFlow<List<OpcionItem>>(emptyList())
    val clientes: StateFlow<List<OpcionItem>> = _clientes.asStateFlow()

    private val _paquetes = MutableStateFlow<List<OpcionItem>>(emptyList())
    val paquetes: StateFlow<List<OpcionItem>> = _paquetes.asStateFlow()

    private val _estados = MutableStateFlow<List<OpcionItem>>(emptyList())
    val estados: StateFlow<List<OpcionItem>> = _estados.asStateFlow()

    private val _tiposPago = MutableStateFlow<List<OpcionItem>>(emptyList())
    val tiposPago: StateFlow<List<OpcionItem>> = _tiposPago.asStateFlow()

    /** Carga clientes, paquetes, estados y tipos de pago. */
    fun cargarCatalogos(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val errores = mutableListOf<String>()
            try {
                val rc = repository.obtenerClientes(token)
                if (rc.isSuccessful) {
                    _clientes.value = (rc.body() ?: emptyList()).map {
                        OpcionItem(it.id, it.name.ifBlank { "Cliente #${it.id}" })
                    }
                } else errores.add("Clientes ${rc.code()}")

                val rp = repository.obtenerPaquetes(token)
                if (rp.isSuccessful) {
                    _paquetes.value = (rp.body() ?: emptyList()).map {
                        OpcionItem(it.id, it.nombre ?: "Paquete #${it.id}")
                    }
                } else errores.add("Paquetes ${rp.code()}")

                val re = repository.obtenerEstados(token)
                if (re.isSuccessful) {
                    _estados.value = (re.body() ?: emptyList()).map {
                        OpcionItem(it.id, it.nombre ?: "Estado #${it.id}")
                    }
                } else errores.add("Estados ${re.code()}")

                val rt = repository.obtenerTiposPago(token)
                if (rt.isSuccessful) {
                    _tiposPago.value = (rt.body() ?: emptyList()).map {
                        OpcionItem(it.id, it.nombre ?: "Pago #${it.id}")
                    }
                } else errores.add("Tipos de pago ${rt.code()}")

                _errorMessage.value =
                    if (errores.isEmpty()) null else "Error al cargar: ${errores.joinToString(", ")}"
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar catálogos: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Obtener todas las reservas del panel (con 1 reintento)
    fun cargarReservas(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            var error: String? = null
            for (intento in 1..2) {
                try {
                    val response = repository.obtenerReservas(token)
                    if (response.isSuccessful) {
                        _reservas.value = response.body() ?: emptyList()
                        error = null
                    } else {
                        error = "Error al cargar: ${response.errorBody()?.string()}"
                    }
                    break
                } catch (e: Exception) {
                    error = "Excepción: ${e.localizedMessage}"
                }
            }
            _errorMessage.value = error
            _isLoading.value = false
        }
    }

    // Crear una nueva reserva
    fun crearReserva(token: String, request: ReservaRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.crearReserva(token, request)
                if (response.isSuccessful) {
                    _errorMessage.value = null
                    cargarReservas(token)
                    onComplete(true)
                } else {
                    _errorMessage.value = "Error al crear: ${response.errorBody()?.string()}"
                    onComplete(false)
                }
            } catch (e: ProtocolException) {
                _errorMessage.value = null
                cargarReservas(token)
                onComplete(true)
            } catch (e: Exception) {
                _errorMessage.value = "Excepción: ${e.localizedMessage}"
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Edita una reserva en dos pasos:
     * 1) PUT con cliente, paquete, pago y vendedor.
     * 2) PUT update_status, solo si estadoNuevoId no es null (el estado cambió).
     * onComplete(exito, mensajeError)
     */
    fun editarReserva(
        token: String,
        id: Int,
        campos: HashMap<String, Any>,
        estadoNuevoId: Int?,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Paso 1: datos generales
                try {
                    val r1 = repository.actualizarReserva(token, id, campos)
                    Log.d("DEBUG_ESTADO", "PUT reserva: code=${r1.code()} enviado=$campos")
                    if (!r1.isSuccessful) {
                        val err = r1.errorBody()?.string()
                        Log.d("DEBUG_ESTADO", "PUT reserva error: $err")
                        onComplete(false, "Error al actualizar datos (${r1.code()}): $err")
                        return@launch
                    }
                } catch (e: ProtocolException) {
                    Log.d("DEBUG_ESTADO", "PUT reserva: ProtocolException (se asume ok)")
                }

                // Paso 2: estado (solo si cambió)
                if (estadoNuevoId != null) {
                    try {
                        val r2 = repository.actualizarEstado(token, id, estadoNuevoId)
                        Log.d("DEBUG_ESTADO", "update_status: code=${r2.code()} status_id=$estadoNuevoId")
                        if (!r2.isSuccessful) {
                            val err = r2.errorBody()?.string()
                            Log.d("DEBUG_ESTADO", "update_status error: $err")
                            onComplete(false, "Error al cambiar estado (${r2.code()}): $err")
                            return@launch
                        }
                    } catch (e: ProtocolException) {
                        Log.d("DEBUG_ESTADO", "update_status: ProtocolException (se asume ok)")
                    }
                }

                _errorMessage.value = null
                cargarReservas(token)
                onComplete(true, null)
            } catch (e: Exception) {
                Log.d("DEBUG_ESTADO", "Excepción: ${e.localizedMessage}")
                onComplete(false, "Excepción: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Eliminar una reserva
    fun eliminarReserva(token: String, id: Int, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.eliminarReserva(token, id)
                if (response.isSuccessful) {
                    _reservas.value = _reservas.value.filter { it.id != id }
                    _errorMessage.value = null
                    cargarReservas(token)
                    onComplete(true)
                } else {
                    _errorMessage.value = "Error al eliminar: ${response.errorBody()?.string()}"
                    onComplete(false)
                }
            } catch (e: ProtocolException) {
                _reservas.value = _reservas.value.filter { it.id != id }
                _errorMessage.value = null
                cargarReservas(token)
                onComplete(true)
            } catch (e: Exception) {
                _errorMessage.value = "Excepción: ${e.localizedMessage}"
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}