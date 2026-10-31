package com.example.travelease.repository

import com.example.travelease.model.CatalogoItemDto
import com.example.travelease.model.ClienteDto
import com.example.travelease.model.ReservaAdminDto
import com.example.travelease.model.ReservaRequest
import com.example.travelease.model.RespuestaPaginadaDto
import com.example.travelease.network.RetrofitClient
import retrofit2.Response

class ReservaPanelRepository {

    private val api get() = RetrofitClient.apiService

    private fun auth(token: String) =
        if (token.startsWith("Token ")) token else "Token $token"

    // ---- Reservas ----

    // Ahora devuelve la respuesta paginada {"data": [...], "pagination": {...}}
    suspend fun obtenerReservas(
        token: String,
        page: Int = 1
    ): Response<RespuestaPaginadaDto<ReservaAdminDto>> =
        api.obtenerReservasPanel(auth(token), page)

    suspend fun crearReserva(
        token: String,
        reservaRequest: ReservaRequest
    ): Response<ReservaAdminDto> =
        api.crearReservaPanel(auth(token), reservaRequest)

    suspend fun actualizarReserva(
        token: String,
        id: Int,
        campos: java.util.HashMap<String, Any>
    ): Response<ReservaAdminDto> =
        api.actualizarReservaPanel(auth(token), id, campos)

    suspend fun actualizarEstado(
        token: String,
        id: Int,
        statusId: Int
    ): Response<Void> =
        api.actualizarEstadoReservaPanel(
            token = auth(token),
            id = id,
            body = hashMapOf("status_id" to statusId)
        )

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