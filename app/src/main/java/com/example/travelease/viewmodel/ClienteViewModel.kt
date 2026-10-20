package com.example.travelease.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelease.model.ClienteActualizarRequestDto
import com.example.travelease.model.ClienteDto
import com.example.travelease.model.ClienteRequestDto
import com.example.travelease.model.EncuestaDto
import com.example.travelease.model.LoginDto
import com.example.travelease.model.Reserva
import com.example.travelease.model.ValoracionDto
import com.example.travelease.model.SugerenciaDto
import com.example.travelease.repository.ClienteRepository
import com.example.travelease.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel : ViewModel() {

    // Empezamos con el token vacío en memoria
    companion object {
        var authToken: String = ""
    }

    private val repository = ClienteRepository()

    private val _estadoMensaje = MutableStateFlow("")
    val estadoMensaje: StateFlow<String> = _estadoMensaje

    private val _listaReservas = MutableStateFlow<List<Reserva>>(emptyList())
    val listaReservas: StateFlow<List<Reserva>> = _listaReservas

    // ESTADO PARA LOS CLIENTES (LECTURA)
    private val _listaClientes = MutableStateFlow<List<ClienteDto>>(emptyList())
    val listaClientes: StateFlow<List<ClienteDto>> = _listaClientes

    var loginMessage = mutableStateOf("")
        private set

    fun limpiarMensaje() {
        loginMessage.value = ""
    }

    var viajesAValorar = mutableStateListOf<Reserva>()
        private set

    // 1. Función para Iniciar Sesión
    fun iniciarSesion(
        context: Context,
        correo: String,
        clave: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val credenciales = LoginDto(username = correo, password = clave)
                val response = RetrofitClient.apiService.login(credenciales)

                if (response.isSuccessful) {
                    val loginData = response.body()
                    val tokenLimpio = loginData?.token ?: ""
                    val rolUsuario = loginData?.rol ?: "cliente"
                    val nombreUsuario = loginData?.username ?: correo

                    // 1. Guardamos en la memoria temporal
                    authToken = "Token $tokenLimpio"

                    // 2. 💾 PERSISTENCIA: Guardamos en el disco duro del teléfono
                    val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
                    sharedPreferences.edit().apply {
                        putString("TOKEN", tokenLimpio)
                        putString("ROL", rolUsuario)
                        putString("NOMBRE", nombreUsuario)
                        apply()
                    }

                    loginMessage.value = "¡Login Exitoso! Bienvenido $rolUsuario"
                    println("✅ MÁSTER DEBUG: Token y datos guardados exitosamente en SharedPreferences")

                    onSuccess(rolUsuario)

                } else {
                    loginMessage.value = "Error: Credenciales incorrectas."
                    println("🚨 Error de login: ${response.code()}")
                }
            } catch (e: Exception) {
                loginMessage.value = "Error de conexión."
                println("🚨 Fallo de red en login: ${e.message}")
            }
        }
    }

    // 2. Función para obtener las reservas
    fun cargarReservas() {
        viewModelScope.launch {
            try {
                println("=== 🚀 ENVIANDO A DJANGO - AUTH_HEADER: '$authToken' ===")
                val response = RetrofitClient.apiService.getReservas(token = authToken)
                if (response.isSuccessful) {
                    _listaReservas.value = response.body() ?: emptyList()
                    println("✅ DATOS RECIBIDOS DE DJANGO: ${response.body()}")
                } else {
                    _estadoMensaje.value = "Error del servidor: ${response.code()}"
                    println("🚨 ERROR AL CARGAR RESERVAS: Código ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _estadoMensaje.value = "Fallo de conexión"
                println("🚨 ERROR FATAL RED: ${e.message}")
            }
        }
    }

    // 3. Función para cargar los clientes reales (GET)
    fun cargarClientes() {
        viewModelScope.launch {
            try {
                println("=== 🚀 CARGANDO CLIENTES DESDE DJANGO ===")
                val response = RetrofitClient.apiService.getClientes(authToken)

                if (response.isSuccessful) {
                    _listaClientes.value = response.body() ?: emptyList()
                    println("✅ Éxito: Clientes descargados correctamente -> ${_listaClientes.value}")
                } else {
                    println("🚨 Error al cargar clientes: Código ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("🚨 Fallo de red al intentar descargar clientes: ${e.message}")
            }
        }
    }

    // --- 4. FUNCIÓN PARA CREAR UN CLIENTE ---
    fun crearCliente(
        nombre: String,
        cedula: String,
        correo: String,
        direccion: String,
        telefono: String,
        departamentoStr: String,
        municipioStr: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val munId = municipioStr.toIntOrNull() ?: 1

                val nuevoCliente = ClienteRequestDto(
                    name = nombre,
                    userEmail = correo,
                    nationalId = cedula,
                    address = direccion,
                    phoneNumber = telefono,
                    municipality = munId
                )

                println("=== 🚀 ENVIANDO NUEVO CLIENTE (REQUEST) A DJANGO ===")
                val response = RetrofitClient.apiService.crearCliente(authToken, nuevoCliente)

                if (response.isSuccessful) {
                    println("✅ ¡Cliente registrado con éxito en la base de datos!")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                    println("🚨 Error al crear cliente: Código ${response.code()} | Detalle de Django: $errorBody")
                    onError("Error del servidor: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("🚨 Excepción de red al crear cliente: ${e.message}")
                onError("Fallo de conexión: ${e.message}")
            }
        }
    }

    // --- 4.1. FUNCIÓN PARA ACTUALIZAR UN CLIENTE EXISTENTE ---
    fun actualizarCliente(
        clienteId: Int,
        nombre: String,
        cedula: String,
        direccion: String,
        telefono: String,
        departamentoStr: String,
        municipioStr: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val munId = municipioStr.toIntOrNull() ?: 1

                val request = ClienteActualizarRequestDto(
                    name = nombre,
                    nationalId = cedula,
                    address = direccion,
                    phoneNumber = telefono,
                    municipality = munId
                )

                println("=== 🚀 ENVIANDO ACTUALIZACIÓN DE CLIENTE A DJANGO ===")
                val response = RetrofitClient.apiService.actualizarCliente(authToken, clienteId, request)

                if (response.isSuccessful) {
                    println("✅ ¡Cliente actualizado con éxito!")
                    cargarClientes() // Refrescar la lista de clientes automáticamente
                    onSuccess()
                } else {
                    // 🚀 MAGIA: Extraemos el error exacto de Django para saber qué falla
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    println("🚨 Error al actualizar cliente: Código ${response.code()} | Detalle: $errorBody")
                    onError("Error de Django: $errorBody")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("🚨 Excepción de red al actualizar cliente: ${e.message}")
                onError("Error de conexión: ${e.message}")
            }
        }
    }

    // 5. Función para enviar valoraciones
    fun enviarvaloracion(
        idDeLaReserva: Int,
        paquete: String,
        estrellas: Int,
        comentario: String
    ) {
        _estadoMensaje.value = "Enviando valoración, por favor espera..."

        viewModelScope.launch {
            try {
                val nuevaValoracion = ValoracionDto(
                    idCliente = 1,
                    cedulaCliente = "288-130788-0000E",
                    nombreCliente = "Jonathan",
                    numeroTelefono = "87414594",
                    idReserva = idDeLaReserva,
                    puntaje = estrellas,
                    comentario = comentario,
                    nombre = paquete,
                    descripcion = "Reseña desde la app",
                    precio = 5000.0
                )

                println("🔑 TOKEN QUE ESTOY ENVIANDO A VALORACIONES: $authToken")
                repository.enviarValoracion(authToken, nuevaValoracion)
                println("✅ ÉXITO: Valoración enviada")

            } catch (e: Exception) {
                e.printStackTrace()
                println("🚨 ERROR AL ENVIAR VALORACIÓN: ${e.message}")
            }
        }
    }

    // 6. Obtener viajes para valorar
    fun obtenerViajesParaValorar() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getReservas(authToken)
                if (response.isSuccessful) {
                    val reservasDelBackend = response.body() ?: emptyList()
                    viajesAValorar.clear()
                    viajesAValorar.addAll(reservasDelBackend)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 7. Función para enviar sugerencias
    fun enviarSugerencia(textoMensaje: String) {
        viewModelScope.launch {
            try {
                val nuevaSugerencia = SugerenciaDto(
                    comentario = textoMensaje,
                    asunto = "Sugerencia desde la App Android"
                )

                println("🔑 TOKEN QUE ESTOY ENVIANDO A SUGERENCIAS: $authToken")
                repository.enviarSugerencia(authToken, nuevaSugerencia)
                println("✅ ÉXITO: Sugerencia enviada por el ViewModel")
            } catch (e: Exception) {
                println("🚨 ERROR AL ENVIAR SUGERENCIA: ${e.message}")
            }
        }
    }

    // 8. Función para enviar encuestas
    fun enviarEncuesta(
        atencionAlCliente: Int,
        facilidadReserva: Int,
        relacionCalidadPrecio: Int,
        calidadServicioProporcionado: Int,
        caracteristicaFav: String,
        informacionProporcionadaSencilla: Int,
        recomendarAOtros: Int,
        volverContratar: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val encuestaDto = EncuestaDto(
                    atencionAlCliente = atencionAlCliente,
                    facilidadReserva = facilidadReserva,
                    relacionCalidadPrecio = relacionCalidadPrecio,
                    calidadServicioProporcionado = calidadServicioProporcionado,
                    caracteristicaFav = caracteristicaFav,
                    informacionProporcionadaSencilla = informacionProporcionadaSencilla,
                    recomendarAOtros = recomendarAOtros,
                    volverContratar = volverContratar
                )

                println("🔑 TOKEN QUE ESTOY ENVIANDO A ENCUESTAS: $authToken")
                val response = RetrofitClient.apiService.postEncuestaSatisfaccion(authToken, encuestaDto)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error ${response.code()}"
                    onError(errorBody)
                }
            } catch (e: Exception) {
                onError("Excepción: ${e.message}")
            }
        }
    }

    // 9. Función para Cerrar Sesión
    fun cerrarSesion(context: Context) {
        loginMessage.value = ""
        authToken = ""

        val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        println("🔒 Sesión cerrada: Memoria temporal y disco duro limpiados.")
    }

}