package com.example.travelease.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importaciones de tus colores de TravelEase
import com.example.travelease.ui.theme.BackgroundLight
import com.example.travelease.ui.theme.BluePrimary
import com.example.travelease.ui.theme.SurfaceWhite
import com.example.travelease.ui.theme.TextPrimary
import com.example.travelease.ui.theme.TextSecondary
import com.example.travelease.ui.theme.ButtonSecondaryBg

// 1. Modelo de datos
data class VueloPrueba(
    val aerolinea: String,
    val origen: String,
    val destino: String,
    val fechaSalida: String,
    val fechaEntrada: String,
    val precio: String
)


@Composable
fun PantallaServiciosVuelos(onBackPressed: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 24.dp)
        ) {
            Text(
                text = "Gestión de Vuelos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            FormularioVuelos()
            Spacer(modifier = Modifier.height(16.dp))
            ListadoVuelos()
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundLight)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .align(Alignment.CenterStart) // Lo pega a la izquierda
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft, // El icono estilo "<"
                    contentDescription = "Retroceder",
                    tint = TextPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

// 3. Tu Formulario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioVuelos() {
    var aerolinea by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fechaSalida by remember { mutableStateOf("") }
    var fechaEntrada by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "FORMULARIO", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Datos del vuelo", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = aerolinea, onValueChange = { aerolinea = it }, label = { Text("Aerolínea") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp))
                OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = origen, onValueChange = { origen = it }, label = { Text("Origen") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp))
                OutlinedTextField(value = destino, onValueChange = { destino = it }, label = { Text("Destino") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = fechaSalida, onValueChange = { fechaSalida = it }, label = { Text("Fecha salida") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp))
                OutlinedTextField(value = fechaEntrada, onValueChange = { fechaEntrada = it }, label = { Text("Fecha entrada") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), shape = RoundedCornerShape(8.dp)) { Text("Agregar") }
                Button(onClick = { aerolinea = ""; precio = ""; origen = ""; destino = ""; fechaSalida = ""; fechaEntrada = "" }, colors = ButtonDefaults.buttonColors(containerColor = ButtonSecondaryBg, contentColor = TextPrimary), shape = RoundedCornerShape(8.dp)) { Text("Limpiar") }
            }
        }
    }
}

// 4. El Listado
@Composable
fun ListadoVuelos() {
    val vuelos = listOf(
        VueloPrueba("Avianca", "Managua", "Miami", "12/10/2026", "20/10/2026", "$350.00"),
        VueloPrueba("Copa Airlines", "Managua", "Panamá", "05/11/2026", "10/11/2026", "$420.00")
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "LISTADO", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Vuelos registrados", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            vuelos.forEach { vuelo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = ButtonSecondaryBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = vuelo.aerolinea, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                            Text(text = vuelo.precio, fontWeight = FontWeight.Bold, color = BluePrimary, fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Ruta: ${vuelo.origen} ➔ ${vuelo.destino}", color = TextSecondary, fontSize = 14.sp)
                        Text(text = "Salida: ${vuelo.fechaSalida} | Regreso: ${vuelo.fechaEntrada}", color = TextSecondary, fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { /* Lógica editar */ }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = BluePrimary)
                            }
                            IconButton(onClick = { /* Lógica eliminar */ }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = androidx.compose.ui.graphics.Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 5. La Vista Previa
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaVuelos() {
    PantallaServiciosVuelos()
}