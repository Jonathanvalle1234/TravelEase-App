package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.travelease.model.Transporte
import com.example.travelease.viewmodel.TransporteViewModel

@Composable
fun TransportesListScreen(
    viewModel: TransporteViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAgregar: () -> Unit,
    onNavigateToEditar: (Int) -> Unit
) {
    val transportes by viewModel.transportes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // Cargar los transportes desde Django al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarTransportes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F5))
            .padding(16.dp)
    ) {
        // Cabecera Azul
        Surface(
            color = Color(0xFF2196F3),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(90.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("TRANSPORTES REGISTRADOS", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Agregar Arriba
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onNavigateToAgregar,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Text("Agregar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista de Transportes o Indicador de Carga
        if (isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2196F3))
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(transportes) { transporte ->
                    TransporteCardItem(
                        transporte = transporte,
                        onEditar = { onNavigateToEditar(transporte.id) },
                        onEliminar = {
                            viewModel.eliminarTransporte(
                                id = transporte.id,
                                onSuccess = {
                                    Toast.makeText(context, "Transporte eliminado", Toast.LENGTH_SHORT).show()
                                },
                                onError = { errorMsg ->
                                    Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Regresar
        IconButton(onClick = onNavigateBack, modifier = Modifier.padding(bottom = 8.dp)) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Regresar", modifier = Modifier.size(36.dp), tint = Color.DarkGray)
        }
    }
}

@Composable
fun TransporteCardItem(
    transporte: Transporte,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    // Cambio a type_transport_name
    val iconoTransporte = when (transporte.type_transport_name.uppercase()) {
        "TAXI" -> "🚕"
        "BUS" -> "🚌"
        "TREN" -> "🚆"
        else -> "🚐"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Cambio a type_transport_name
                Text(text = "$iconoTransporte ${transporte.type_transport_name.uppercase()}", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📍 Ruta: ${transporte.origin} ➔ ${transporte.destination}", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(6.dp))
                // Formateo para evitar nulos en el precio
                val precioMostrar = transporte.current_price ?: 0.0
                Text(text = "💲 Precio: $ $precioMostrar", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier.fillMaxWidth().offset(y = (-4).dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onEditar,
                    modifier = Modifier.size(38.dp).background(Color(0xFFE3F2FD), shape = CircleShape)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF1976D2), modifier = Modifier.size(18.dp))
                }

                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(38.dp).background(Color(0xFFFFEBEE), shape = CircleShape)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}