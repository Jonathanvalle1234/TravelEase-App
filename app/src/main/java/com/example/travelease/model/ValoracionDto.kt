package com.example.travelease.model

data class ValoracionDto(
    val idCliente: Int,
    val cedulaCliente: String,
    val nombreCliente: String,
    val numeroTelefono: String,
    val idReserva: Int,
    val puntaje: Int,
    val comentario: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val vuelo: List<Any> = emptyList()
)