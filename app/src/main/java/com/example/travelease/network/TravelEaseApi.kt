package com.example.travelease.network

import com.example.travelease.model.ClienteDto
import com.example.travelease.model.ClienteRequestDto
import com.example.travelease.model.EncuestaDto
import com.example.travelease.model.LoginDto
import com.example.travelease.model.LoginResponse
import com.example.travelease.model.Reserva
import com.example.travelease.model.SugerenciaDto
import com.example.travelease.model.Transporte
import com.example.travelease.model.TransporteRequest
import com.example.travelease.model.TransporteUpdateDto
import com.example.travelease.model.ValoracionDto
import com.example.travelease.model.Vuelo
import com.example.travelease.model.VueloUpdateDto
import com.example.travelease.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TravelEaseApi {

    @GET("api/clients-mobile/reservas/")
    suspend fun getReservas(
        @Header("Authorization") token: String
    ): Response<List<Reserva>>

    @GET("api/clients/")
    suspend fun getClientes(
        @Header("Authorization") token: String
    ): Response<List<ClienteDto>>

    @POST("api/users/login/")
    suspend fun login(
        @Body request: LoginDto
    ): Response<LoginResponse>

    @POST("api/clients-mobile/sugerencias/")
    suspend fun enviarSugerencia(
        @Header("Authorization") token: String,
        @Body sugerencia: SugerenciaDto
    ): Response<Unit>

    @POST("api/clients-mobile/encuesta-satisfaccion/")
    suspend fun postEncuestaSatisfaccion(
        @Header("Authorization") token: String,
        @Body encuesta: EncuestaDto
    ): retrofit2.Response<Void>

    @POST("api/clients-mobile/valoracion/")
    suspend fun enviarValoracionBackend(
        @Header("Authorization") token: String,
        @Body valoracion: ValoracionDto
    ): Response<Unit>

    @POST("api/clients/")
    suspend fun crearCliente(
        @Header("Authorization") token: String,
        @Body cliente: ClienteRequestDto
    ): Response<ClienteDto>

    @PUT("api/clients/{id}/")
    suspend fun actualizarCliente(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body cliente: ClienteRequestDto
    ): retrofit2.Response<ClienteDto>

    @GET("api/services/flights/")
    suspend fun obtenerVuelos(
        @Header("Authorization") token: String
    ): Response<List<Vuelo>>

    @POST("api/services/flights/")
    suspend fun crearVuelo(
        @Header("Authorization") token: String,
        @Body vuelo: com.example.travelease.model.VueloRequestDto
    ): Response<Vuelo>

    @PUT("api/services/flights/{id}/")
    suspend fun actualizarVuelo(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body vuelo: com.example.travelease.model.VueloRequestDto
    ): Response<Vuelo>

    @POST("api/services/flights/{id}/prices/")
    suspend fun actualizarPrecioVuelo(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body priceMap: Map<String, Double>
    ): Response<Void>

    @PUT("api/services/flights/{id}/")
    suspend fun actualizarVueloParcial(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body vuelo: VueloUpdateDto
    ): Response<Vuelo>

    @DELETE("api/services/flights/{id}/")
    suspend fun eliminarVuelo(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Void>

    // 🚀 CORREGIDO: "catalogos" en lugar de "catalogs" (como lo tienes en Bruno)
    @GET("api/catalogos/type-transports/")
    suspend fun obtenerTiposTransporte(
        @Header("Authorization") token: String
    ): Response<List<TipoTransporte>>

    @GET("api/services/transportation/")
    suspend fun obtenerTransportes(
        @Header("Authorization") token: String
    ): Response<List<Transporte>>

    @POST("api/services/transportation/")
    suspend fun crearTransporte(
        @Header("Authorization") token: String,
        @Body req: TransporteRequest
    ): Response<Transporte>

    @PATCH("api/services/transportation/{id}/")
    suspend fun actualizarTransporte(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body req: TransporteUpdateDto
    ): Response<Transporte>

    @POST("api/services/transportation/{id}/prices/")
    suspend fun actualizarPrecioTransporte(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body priceMap: Map<String, Double>
    ): Response<Void>

    @DELETE("api/services/transportation/{id}/")
    suspend fun eliminarTransporte(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Void>
}