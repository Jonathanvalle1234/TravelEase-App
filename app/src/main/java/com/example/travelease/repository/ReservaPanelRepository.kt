package com.example.travelease.repository

import com.example.travelease.model.CatalogoItemDto
import com.example.travelease.model.ClienteDto
import com.example.travelease.model.ReservaAdminDto
import com.example.travelease.model.ReservaRequest
import com.example.travelease.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Response

class ReservaPanelRepository {

    private val api get() = RetrofitClient.apiService

    // Agrega "Token " solo si no lo tiene, para no duplicarlo
    private fun auth(token: String) =
        if (token.startsWith("Token ")) token else "Token $token"

    // ---- Reservas ----
    suspend fun obtenerReservas(token: String): Response<List<ReservaAdminDto>> =
        api.obtenerReservasPanel(auth(token))

    suspend fun crearReserva(token: String, reservaRequest: ReservaRequest): Response<ReservaAdminDto> =
        api.crearReservaPanel(auth(token), reservaRequest)

    // PUT: client, package_id, payment_type, seller_user
    suspend fun actualizarReserva(
        token: String,
        id: Int,
        campos: java.util.HashMap<String, Any>
    ): Response<ReservaAdminDto> =
        api.actualizarReservaPanel(auth(token), id, campos)

    // PUT .../{id}/update_status/ con {"status_id": X}
    suspend fun actualizarEstado(
        token: String,
        id: Int,
        statusId: Int
    ): Response<ResponseBody> =
        api.actualizarEstadoReservaPanel(auth(token), id, hashMapOf("status_id" to statusId))

    suspend fun eliminarReserva(token: String, id: Int): Response<Void> =
        api.eliminarReservaPanel(auth(token), id)

    // ---- Catálogos ----
    suspend fun obtenerClientes(token: String): Response<List<ClienteDto>> =
        api.getClientes(auth(token))

    suspend fun obtenerPaquetes(token: String): Response<List<CatalogoItemDto>> =
        api.getPaquetesOpciones(auth(token))

    suspend fun obtenerEstados(token: String): Response<List<CatalogoItemDto>> =
        api.getEstadosReservaOpciones(auth(token))

    suspend fun obtenerTiposPago(token: String): Response<List<CatalogoItemDto>> =
        api.getTiposPagoOpciones(auth(token))
}