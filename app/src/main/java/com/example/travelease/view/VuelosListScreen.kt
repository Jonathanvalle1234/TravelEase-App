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
import com.example.travelease.model.Vuelo
import com.example.travelease.viewmodel.VueloViewModel

@Composable
fun VuelosListScreen(
    viewModel: VueloViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAgregar: () -> Unit,
    onNavigateToEditar: (Int) -> Unit
) {
    val vuelos by viewModel.vuelos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarVuelos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F5))
            .padding(16.dp)
    ) {
        // --- Cabecera Azul ---
        Surface(
            color = Color(0xFF2196F3),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(90.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "Vuelos Registrados",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🚀 BOTÓN "AGREGAR" MOVIDO ARRIBA (Alineado a la derecha)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onNavigateToAgregar,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Text("Agregar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Lista de Vuelos ---
        if (isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2196F3))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(vuelos) { vuelo ->
                    VueloCardItem(
                        vuelo = vuelo,
                        onEditar = { onNavigateToEditar(vuelo.id) },
                        onEliminar = {
                            viewModel.eliminarVuelo(
                                vueloId = vuelo.id,
                                onSuccess = {
                                    Toast.makeText(context, "¡Vuelo borrado con éxito!", Toast.LENGTH_SHORT).show()
                                },
                                onError = { errorMsg ->
                                    Toast.makeText(context, "Error al borrar: $errorMsg", Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Botón de Regresar (Abajo) ---
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Regresar",
                modifier = Modifier.size(36.dp),
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun VueloCardItem(
    vuelo: Vuelo,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val fechaSalidaFormato = vuelo.fechaSalida.substringBefore("T")
    val fechaEntradaFormato = vuelo.fechaEntrada.substringBefore("T")

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "✈️ Aerolínea: ${vuelo.aerolinea}", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📍 Ruta: ${vuelo.origen} ➔ ${vuelo.destino}", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📅 Salida: $fechaSalidaFormato", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📅 Entrada: $fechaEntradaFormato", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "💲 Precio: $ ${vuelo.precio}", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ✏️ Botón de Editar (Lápiz) -> Lado Izquierdo
                IconButton(
                    onClick = onEditar,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFE3F2FD), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Vuelo",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // 🗑️ Botón de Eliminar (Basurero) -> Lado Derecho
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFFFEBEE), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Vuelo",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}