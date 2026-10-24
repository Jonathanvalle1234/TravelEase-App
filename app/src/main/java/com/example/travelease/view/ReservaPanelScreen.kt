package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.viewmodel.ReservaPanelViewModel

private val FondoPantalla = Color(0xFFE9EFF8)
private val AzulHeader = Color(0xFF1E90F0)
private val BordeOscuro = Color(0xFF2B2B2B)

@Composable
fun ReservaPanelScreen(
    viewModel: ReservaPanelViewModel,
    token: String,
    onNavigateToAgregar: () -> Unit,
    onNavigateToEditar: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val reservas by viewModel.reservas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) { viewModel.cargarReservas(token) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        // 1. Header azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    AzulHeader,
                    RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "RESERVAS REGISTRADAS",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            Spacer(Modifier.height(14.dp))

            // 2. Botón Agregar
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = onNavigateToAgregar,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, BordeOscuro),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        "Agregar",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            errorMessage?.let {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 12.sp
                    )
                }
            }

            // 3. Lista
            Box(Modifier.weight(1f).fillMaxWidth()) {
                when {
                    isLoading && reservas.isEmpty() -> {
                        CircularProgressIndicator(
                            color = AzulHeader,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    reservas.isEmpty() -> {
                        Text(
                            text = "No hay reservas registradas",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            items(reservas) { reserva ->
                                // Temporal: sin bloqueo para poder probar Editar en cualquier reserva
                                val bloqueada = false
                                ReservaCard(
                                    cliente = reserva.nombreCliente,
                                    paquete = reserva.tituloPaquete,
                                    precio = reserva.precio,
                                    estado = reserva.estado ?: "Pendiente",
                                    editarHabilitado = !bloqueada,
                                    onEdit = { onNavigateToEditar(reserva.id) },
                                    onDelete = {
                                        viewModel.eliminarReserva(token, reserva.id) { exito ->
                                            if (exito) {
                                                Toast.makeText(context, "🗑️ Reserva eliminada con éxito", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "❌ Error al eliminar reserva", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 4. Flecha de regreso
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onNavigateBack() },
                contentAlignment = Alignment.CenterStart
            ) {
                Canvas(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(width = 8.dp, height = 15.dp)
                ) {
                    val grosor = 1.5.dp.toPx()
                    drawLine(
                        color = Color.DarkGray,
                        start = Offset(size.width, 0f),
                        end = Offset(0f, size.height / 2),
                        strokeWidth = grosor,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color.DarkGray,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height),
                        strokeWidth = grosor,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
fun ReservaCard(
    cliente: String,
    paquete: String,
    precio: Double,
    estado: String,
    editarHabilitado: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val forma = RoundedCornerShape(10.dp)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        // Caja de datos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, forma)
                .border(1.dp, BordeOscuro, forma)
                .padding(horizontal = 10.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("👤 Cliente: $cliente", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
            Text("✈️ Paquete: $paquete", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
            Text("🎫 Precio: $${"%.2f".format(precio)}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
            Text("📌 Estado: $estado", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
        }

        // Caja de acciones con botones circulares de color
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White, forma)
                .border(1.dp, BordeOscuro, forma)
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Editar: círculo azul, lápiz amarillo
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (editarHabilitado) Color(0xFF2196F3) else Color.LightGray)
                    .clickable(enabled = editarHabilitado) { onEdit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color(0xFFFFD600),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Botón Eliminar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFDE8E8))
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}