package com.example.travelease.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.AttachMoney // <-- Icono de dinero actualizado
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.model.PaqueteDto
import com.example.travelease.viewmodel.PaquetesViewModel

@Composable
fun PaquetesListScreen(
    token: String, // Recibimos el token del usuario logueado
    viewModel: PaquetesViewModel,
    onAgregarClick: () -> Unit = {},
    onEditClick: (PaqueteDto) -> Unit = {},
    onDeleteClick: (PaqueteDto) -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    LaunchedEffect(Unit) {
        viewModel.obtenerPaquetes(token)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F9))
    ) {
        // --- Header Azul ---
        Surface(
            color = Color(0xFF2196F3),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = Modifier.fillMaxWidth().height(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "PAQUETES TURISTICOS REGISTRADOS",
                    color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
            }
        }

        // --- Botón Agregar ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onAgregarClick,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Text("Agregar", fontWeight = FontWeight.Bold)
            }
        }


        if (viewModel.isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                items(viewModel.paquetesList) { paquete ->
                    PaqueteCardItemReal(
                        paquete = paquete,
                        onEditClick = { onEditClick(paquete) },
                        onDeleteClick = { onDeleteClick(paquete) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // --- Botón Atrás ---
        IconButton(onClick = onBackClick, modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Atrás", tint = Color.Gray, modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun PaqueteCardItemReal(
    paquete: PaqueteDto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column {
        // Título del paquete en mayúsculas
        Text(
            text = (paquete.titulo ?: "SIN TÍTULO").uppercase(),
            fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.Black,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = paquete.descripcion ?: "Sin descripción",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "País",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "País: ${paquete.pais}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // --- Fila para Ciudad con Icono ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Ciudad",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Ciudad: ${paquete.ciudad}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(4.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney, // Icono de dinero
                            contentDescription = "Total",
                            tint = Color(0xFF4CAF50), // Color verde para indicar dinero/total
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Total: $${String.format("%.2f", paquete.precioCalculado)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = Color.Black)

                // --- Botones de Editar y Eliminar con Círculos de Fondo ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón Editar con fondo circular amarillo
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFFFECB3),
                        modifier = Modifier.size(40.dp)
                    ) {
                        IconButton(onClick = onEditClick, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color(0xFFFF8F00),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Botón Eliminar con fondo circular rojo
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFFFCDD2),
                        modifier = Modifier.size(40.dp)
                    ) {
                        IconButton(onClick = onDeleteClick, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}