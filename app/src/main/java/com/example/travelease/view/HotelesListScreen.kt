package com.example.travelease.view

import android.widget.Toast // 🔥 Importante para el mensaje flotante
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.model.Hotel
import com.example.travelease.viewmodel.HotelViewModel

@Composable
fun HotelesListScreen(
    viewModel: HotelViewModel,
    onNavigateToForm: (Int?) -> Unit,
    onNavigateBack: () -> Unit
) {
    val hoteles by viewModel.hoteles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.obtenerHoteles(context)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F4F8))) {

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color(0xFF2196F3), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "HOTELES REGISTRADOS",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { onNavigateToForm(null) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) { Text("Agregar", fontWeight = FontWeight.Bold) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (hoteles.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { Text("No hay hoteles", color = Color.Gray) }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(hoteles) { hotel ->
                        HotelFigmaCard(
                            hotel = hotel,
                            onEdit = { onNavigateToForm(hotel.id) },
                            onDelete = {
                                // 🔥 MAGIA AQUÍ: Configuramos los mensajes de éxito y error al eliminar
                                viewModel.eliminarHotel(
                                    context = context,
                                    id = hotel.id,
                                    onSuccess = {
                                        Toast.makeText(context, "Hotel eliminado con éxito", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }

        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Atrás",
                tint = Color.DarkGray,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun HotelFigmaCard(hotel: Hotel, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text(text = "🏨 Hotel: ${hotel.nombre}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📍 Ciudad: ${hotel.ciudad}", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "⭐ Estrellas: ${hotel.estrellas}", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                if (hotel.precioActual > 0) {
                    Text(text = "💲 Precio: $ ${hotel.precioActual}", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
            Divider(color = Color.Black, thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Editar (Lápiz Azul)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD))
                        .clickable { onEdit() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                }

                // Eliminar (Basurero Rojo)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBEE))
                        .clickable { onDelete() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}