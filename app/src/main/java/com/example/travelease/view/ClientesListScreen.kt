package com.example.travelease.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importamos ClienteDto y el ViewModel
import com.example.travelease.model.ClienteDto
import com.example.travelease.viewmodel.ClienteViewModel

@Composable
fun ClientesListScreen(
    viewModel: ClienteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToNuevo: () -> Unit,
    onNavigateToEditar: (Int) -> Unit, // 🚀 CAMBIO 1: Ahora recibe el ID del cliente (Int)
    onNavigateBack: () -> Unit
) {

    val clientes by viewModel.listaClientes.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.cargarClientes()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Encabezado Azul
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF2196F3),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = "Clientes Registrados",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Lista Dinámica de Tarjetas Reales
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(clientes) { cliente ->
                    ClienteCard(
                        cliente = cliente,
                        // 🚀 CAMBIO 2: Le pasamos el ID del cliente al hacer clic
                        onEditarClick = { onNavigateToEditar(cliente.id) }
                    )
                }
            }
        }

        // Botón "Agregar" flotante
        ExtendedFloatingActionButton(
            onClick = onNavigateToNuevo,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Text("Agregar", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ClienteCard(cliente: ClienteDto, onEditarClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom, // Lápiz alineado abajo
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f)) {
                // 1. Nombres
                Text(text = "👤 ${cliente.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                // 2. Correo
                Text(text = "📧 ${cliente.userEmail}", fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                // 3. Número
                Text(text = "📱 ${cliente.phoneNumber}", fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                // 4. Cédula
                Text(text = "🆔 ${cliente.nationalId}", fontSize = 14.sp, color = Color.DarkGray)
            }

            IconButton(onClick = onEditarClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF2196F3))
            }
        }
    }
}