package com.example.travelease.model

import com.google.gson.annotations.SerializedName
import com.google.gson.JsonElement

data class DestinationDto(
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?
)

data class PaqueteDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName(value = "titulo", alternate = ["title", "nombre_paquete", "nombre", "name"])
    val titulo: String? = "",

    @SerializedName(value = "descripcion", alternate = ["description"])
    val descripcion: String? = "",

    @SerializedName("destination")
    val destination: DestinationDto? = null,

    @SerializedName(value = "precio", alternate = ["price", "cost", "valor", "monto"])
    val precioDirecto: Double = 0.0,

    // Capturamos el JSON crudo de los servicios tal como viene de Django
    @SerializedName(value = "services", alternate = ["details"])
    val rawServices: JsonElement? = null
) {
    val pais: String
        get() = destination?.country ?: "N/A"

    val ciudad: String
        get() = destination?.city ?: "N/A"

    val precioCalculado: Double
        get() {
            var sumaServicios = 0.0

            try {
                if (rawServices != null && rawServices.isJsonObject) {
                    val obj = rawServices.asJsonObject

                    // 1. Buscamos en el arreglo de "flights" (vuelos) que Django envió
                    if (obj.has("flights") && obj.get("flights").isJsonArray) {
                        val flightsArray = obj.get("flights").asJsonArray
                        for (i in 0 until flightsArray.size()) {
                            val flightItem = flightsArray[i].asJsonObject
                            if (flightItem.has("total") && !flightItem.get("total").isJsonNull) {
                                sumaServicios += flightItem.get("total").asDouble
                            }
                        }
                    }

                    // 2. Buscamos en el arreglo de "hotels" por si acaso
                    if (obj.has("hotels") && obj.get("hotels").isJsonArray) {
                        val hotelsArray = obj.get("hotels").asJsonArray
                        for (i in 0 until hotelsArray.size()) {
                            val hotelItem = hotelsArray[i].asJsonObject
                            if (hotelItem.has("total") && !hotelItem.get("total").isJsonNull) {
                                sumaServicios += hotelItem.get("total").asDouble
                            }
                        }
                    }

                    // 3. Buscamos en el arreglo de "transportation" por si acaso
                    if (obj.has("transportation") && obj.get("transportation").isJsonArray) {
                        val transArray = obj.get("transportation").asJsonArray
                        for (i in 0 until transArray.size()) {
                            val transItem = transArray[i].asJsonObject
                            if (transItem.has("total") && !transItem.get("total").isJsonNull) {
                                sumaServicios += transItem.get("total").asDouble
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error sumando servicios: ${e.message}")
            }

            // Retornamos la suma de los servicios de los vuelos/hoteles,
            // o el precio directo si existe, o un respaldo de 500.0 por ley
            return when {
                sumaServicios > 0 -> sumaServicios
                precioDirecto > 0 -> precioDirecto
                else -> 500.0
            }
        }
}