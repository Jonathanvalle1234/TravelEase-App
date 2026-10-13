package com.example.travelease.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelease.R
import com.example.travelease.viewmodel.ClienteViewModel
// 🚀 1. NUEVAS IMPORTACIONES
import com.example.travelease.viewmodel.TransporteViewModel
import com.example.travelease.viewmodel.VueloViewModel

@Composable
fun MenuAsistenteScreen(
    // 🚀 2. INYECTAMOS LOS VIEWMODELS
    clienteViewModel: ClienteViewModel = viewModel(),
    transporteViewModel: TransporteViewModel = viewModel(),
    vueloViewModel: VueloViewModel = viewModel(),
    onNavigateToClientes: () -> Unit,
    onNavigateToServicios: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    // 3. Obtenemos las listas en tiempo real
    val listaClientes by clienteViewModel.listaClientes.collectAsState()
    val listaTransportes by transporteViewModel.transportes.collectAsState()
    val listaVuelos by vueloViewModel.vuelos.collectAsState()

    // 🚀 4. CALCULAMOS EL TOTAL DE SERVICIOS DINÁMICAMENTE
    val totalServicios = listaTransportes.size + listaVuelos.size

    // 5. Cargamos todos los datos automáticamente al abrir la pantalla
    LaunchedEffect(Unit) {
        clienteViewModel.cargarClientes()
        transporteViewModel.cargarTransportes()
        vueloViewModel.cargarVuelos()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))
    ) {
        // Encabezado Azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF2196F3),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Asistente", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Cuadrícula de opciones
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DashboardCard(
                    titulo = "Clientes",
                    cantidad = listaClientes.size.toString(),
                    iconoRes = R.drawable.icono_clientes_2,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigateToClientes()
                }
                DashboardCard(
                    titulo = "Servicios",
                    // 🚀 6. PONEMOS LA VARIABLE REAL AQUÍ
                    cantidad = totalServicios.toString(),
                    iconoRes = R.drawable.icono_servicios,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigateToServicios()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DashboardCard(
                    titulo = "Paquetes",
                    cantidad = "10", // Pendiente para cuando hagas los paquetes
                    iconoRes = R.drawable.icono_paquete_turisticos,
                    modifier = Modifier.weight(1f)
                ) { }
                DashboardCard(
                    titulo = "Reservas",
                    cantidad = "5", // Pendiente para cuando hagas las reservas
                    iconoRes = R.drawable.icono_reservas,
                    modifier = Modifier.weight(1f)
                ) { }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Cerrar Sesión
        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.padding(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun DashboardCard(
    titulo: String,
    cantidad: String,
    iconoRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconoRes),
                contentDescription = titulo,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = titulo, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Text(text = cantidad, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}