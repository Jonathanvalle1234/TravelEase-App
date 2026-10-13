package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelease.viewmodel.TransporteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransporteFormScreen(
    modo: String,
    transporteId: Int,
    viewModel: TransporteViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val tiposTransporte by viewModel.tiposTransporte.collectAsState()

    var tipoId by remember { mutableStateOf(0) }
    var tipoTexto by remember { mutableStateOf("") }
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    // Carga de datos si el formulario está en modo edición y descarga el catálogo
    LaunchedEffect(transporteId) {
        viewModel.cargarTiposTransporte()

        if (modo == "editar" && transporteId != 0) {
            val transporteEdit = viewModel.transportes.value.find { it.id == transporteId }
            transporteEdit?.let {
                tipoId = it.type_transport
                tipoTexto = it.type_transport_name.uppercase()
                origen = it.origin
                destino = it.destination
                precio = it.current_price?.toString() ?: "0.0"
            }
        }
    }

    val tituloPantalla = if (modo == "editar") "EDITAR TRANSPORTE" else "DATOS DEL TRANSPORTE"
    val textoBotonPrimario = if (modo == "editar") "Actualizar" else "Agregar"

    // 🚀 NUEVO: Variable para controlar el desplazamiento (scroll)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F5))
            // 🚀 NUEVO: Configuraciones para el Scroll y el Teclado
            .verticalScroll(scrollState)
            .imePadding() // Empuja la pantalla hacia arriba cuando el teclado aparece
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera Azul
        Surface(
            color = Color(0xFF2196F3),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = tituloPantalla,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón principal alineado a la derecha (debajo de la cabecera)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = {
                    if (tipoId == 0 || origen.isBlank() || destino.isBlank() || precio.isBlank()) {
                        Toast.makeText(context, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show()
                        return@OutlinedButton
                    }
                    viewModel.guardarTransporte(
                        modo = modo,
                        transporteId = transporteId,
                        tipoId = tipoId,
                        origen = origen,
                        destino = destino,
                        precioStr = precio,
                        onSuccess = {
                            Toast.makeText(context, "Transporte guardado con éxito", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        onError = { error ->
                            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                enabled = !isLoading
            ) {
                Text(textoBotonPrimario, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown para Tipo de Transporte
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = tipoTexto,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Transporte", fontWeight = FontWeight.Bold, color = Color.Black) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color(0xFF2196F3)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tiposTransporte.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo.name.uppercase()) },
                        onClick = {
                            tipoId = tipo.id
                            tipoTexto = tipo.name.uppercase()
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Origen
        OutlinedTextField(
            value = origen,
            onValueChange = { origen = it },
            label = { Text("Origen", fontWeight = FontWeight.Bold, color = Color.Black) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color(0xFF2196F3)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Destino
        OutlinedTextField(
            value = destino,
            onValueChange = { destino = it },
            label = { Text("Destino", fontWeight = FontWeight.Bold, color = Color.Black) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color(0xFF2196F3)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Precio
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio", fontWeight = FontWeight.Bold, color = Color.Black) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color(0xFF2196F3)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones Secundarios (Limpiar y Cancelar)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = {
                    tipoId = 0
                    tipoTexto = ""
                    origen = ""
                    destino = ""
                    precio = ""
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                modifier = Modifier.width(130.dp)
            ) {
                Text("limpiar", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onNavigateBack,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                modifier = Modifier.width(130.dp)
            ) {
                Text("Cancelar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        // 🚀 NUEVO: Reemplazamos el weight(1f) por un espacio fijo de 32.dp
        Spacer(modifier = Modifier.height(32.dp))

        // Botón de retroceso inferior
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Regresar",
                modifier = Modifier.size(36.dp),
                tint = Color.DarkGray
            )
        }
    }
}