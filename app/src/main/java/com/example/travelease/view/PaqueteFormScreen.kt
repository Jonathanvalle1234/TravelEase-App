package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.model.PaqueteDto

private const val PLACEHOLDER_VUELO = "Seleccionar Vuelo"
private const val PLACEHOLDER_HOTEL = "Seleccionar Hotel"
private const val PLACEHOLDER_TRANSPORTE = "Seleccionar Transporte"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaqueteFormScreen(
    paqueteAEditar: PaqueteDto? = null,
    // Listas reales con id y nombre (el id es lo que Django necesita)
    vuelosDisponibles: List<OpcionItem> = emptyList(),
    hotelesDisponibles: List<OpcionItem> = emptyList(),
    transportesDisponibles: List<OpcionItem> = emptyList(),
    onGuardarClick: (
        titulo: String,
        descripcion: String,
        pais: String,
        ciudad: String,
        vueloId: Int?,
        hotelId: Int?,
        transporteId: Int?
    ) -> Unit,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val esEdicion = paqueteAEditar != null

    // Estados de los campos principales
    var titulo by remember { mutableStateOf(paqueteAEditar?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(paqueteAEditar?.descripcion ?: "") }
    var pais by remember { mutableStateOf(paqueteAEditar?.pais.takeIf { it != "N/A" } ?: "") }
    var ciudad by remember { mutableStateOf(paqueteAEditar?.ciudad.takeIf { it != "N/A" } ?: "") }

    // Servicios elegidos (null = nada seleccionado)
    var vueloSel by remember { mutableStateOf<OpcionItem?>(null) }
    var hotelSel by remember { mutableStateOf<OpcionItem?>(null) }
    var transporteSel by remember { mutableStateOf<OpcionItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F9))
            .verticalScroll(rememberScrollState())
    ) {

        Surface(
            color = Color(0xFF2196F3),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = Modifier.fillMaxWidth().height(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = if (esEdicion) "EDITAR PAQUETES TURISTICOS" else "CREAR PAQUETES TURISTICOS",
                    color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // --- Botón de Acción Principal ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        when {
                            titulo.isBlank() -> Toast.makeText(
                                context, "Escribe el nombre del paquete", Toast.LENGTH_SHORT
                            ).show()

                            vueloSel == null && hotelSel == null && transporteSel == null ->
                                Toast.makeText(
                                    context,
                                    "Selecciona al menos un servicio (vuelo, hotel o transporte)",
                                    Toast.LENGTH_LONG
                                ).show()

                            else -> onGuardarClick(
                                titulo, descripcion, pais, ciudad,
                                vueloSel?.id, hotelSel?.id, transporteSel?.id
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text(
                        text = if (esEdicion) "Actualizar" else "Agregar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Campos de Texto ---
            Text("NOMBRE DEL PAQUETES", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("DESCRIPCION", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // País y Ciudad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("País", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = pais,
                        onValueChange = { pais = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White),
                        singleLine = true
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Ciudad", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = ciudad,
                        onValueChange = { ciudad = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("AGREGAR SERVICIOS", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))


            DropdownReal(
                label = "Vuelos Disponibles",
                opciones = if (vuelosDisponibles.isNotEmpty()) vuelosDisponibles.map { it.nombre } else listOf("No hay vuelos registrados"),
                seleccionado = vueloSel?.nombre ?: PLACEHOLDER_VUELO
            ) { texto -> vueloSel = vuelosDisponibles.firstOrNull { it.nombre == texto } }

            Spacer(modifier = Modifier.height(8.dp))

            DropdownReal(
                label = "Hoteles Disponibles",
                opciones = if (hotelesDisponibles.isNotEmpty()) hotelesDisponibles.map { it.nombre } else listOf("No hay hoteles registrados"),
                seleccionado = hotelSel?.nombre ?: PLACEHOLDER_HOTEL
            ) { texto -> hotelSel = hotelesDisponibles.firstOrNull { it.nombre == texto } }

            Spacer(modifier = Modifier.height(8.dp))

            DropdownReal(
                label = "Transporte Disponibles",
                opciones = if (transportesDisponibles.isNotEmpty()) transportesDisponibles.map { it.nombre } else listOf("No hay transportes registrados"),
                seleccionado = transporteSel?.nombre ?: PLACEHOLDER_TRANSPORTE
            ) { texto -> transporteSel = transportesDisponibles.firstOrNull { it.nombre == texto } }

            Spacer(modifier = Modifier.height(16.dp))


            val totalMostrado = if (esEdicion) {
                String.format("%.2f", paqueteAEditar?.precioCalculado ?: 0.0)
            } else {
                "0.00"
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total: $$totalMostrado",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Botones Inferiores ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        titulo = ""
                        descripcion = ""
                        pais = ""
                        ciudad = ""
                        vueloSel = null
                        hotelSel = null
                        transporteSel = null
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Limpiar", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = onBackClick, modifier = Modifier.padding(bottom = 16.dp)) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Atrás", tint = Color.Gray, modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownReal(
    label: String,
    opciones: List<String>,
    seleccionado: String,
    onSeleccionado: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = seleccionado,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion, fontWeight = FontWeight.Bold) },
                    enabled = !opcion.startsWith("No hay"),
                    onClick = {
                        onSeleccionado(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}