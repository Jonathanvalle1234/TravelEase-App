package com.example.travelease.model

// Modelo para el Dropdown (Catálogo)
data class TipoTransporte(
    val id: Int,
    val code: String,
    val name: String,
    val active: Boolean
)

// Cambia la clase Transporte que tienes por esta:
data class Transporte(
    val id: Int,
    val type_transport: Int, // Ahora recibe solo el número del ID
    val type_transport_name: String, // El nombre viene suelto
    val type_transport_code: String, // El código viene suelto
    val origin: String,
    val destination: String,
    val current_price: Double?,
    val active: Boolean
)

// Modelo para Crear (POST)
data class TransporteRequest(
    val type_transport: Int,
    val origin: String,
    val destination: String,
    val price: Double
)

// Modelo para Editar (PATCH - Omitimos el precio)
data class TransporteUpdateDto(
    val type_transport: Int,
    val origin: String,
    val destination: String
)