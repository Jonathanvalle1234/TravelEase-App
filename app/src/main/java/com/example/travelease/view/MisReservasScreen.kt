package com.example.travelease.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.model.Reserva
import com.example.travelease.viewmodel.ClienteViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft // 👈 El icono exacto

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ReservaItemCard(reserva: Reserva, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. Nombre del Paquete
            Text(
                text = reserva.nombre_paquete,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 2. ID de Reserva
            Text(
                text = "ID Reserva: ${reserva.idReserva}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 3. Número de Factura
            Text(
                text = "Factura N°: ${reserva.numero_factura}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 4. Total
            Text(
                text = "Total: C$ ${reserva.total}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun MisReservasScreen(
    viewModel: ClienteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEvaluar: (String, String, String, String) -> Unit
) {
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.cargarReservas()
    }

    val listaReservas by viewModel.listaReservas.collectAsState()
    val estadoMensaje by viewModel.estadoMensaje.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(), // 👈 Escudo protector para la cabecera azul
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- CABECERA AZUL ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        .background(Color(0xFF2196F3))
                        .padding(vertical = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mis Reservas",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- LISTA DE RESERVAS O MENSAJE VACÍO ---
                if (listaReservas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (estadoMensaje.isNotEmpty()) estadoMensaje else "La lista llegó vacía",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    val misDatos: List<Reserva> = listaReservas

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(count = misDatos.size) { index ->
                            val reserva = misDatos[index]

                            ReservaItemCard(
                                reserva = reserva,
                                onClick = {
                                    onNavigateToEvaluar(
                                        reserva.idReserva.toString(),
                                        reserva.nombre_paquete,
                                        reserva.numero_factura,
                                        reserva.total.toString()
                                    )
                                }
                            )
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 16.dp, top = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Regresar",
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

