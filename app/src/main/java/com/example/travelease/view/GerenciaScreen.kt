package com.example.travelease.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import com.example.travelease.R // Importante para leer la carpeta drawable

@Composable
fun GerenciaScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPaquetesVendidos: () -> Unit,
    onNavigateToIngresosDestino: () -> Unit,
    onNavigateToTopClientes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEF2F6))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF1E88E5),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Gerencia", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "Reportes", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            // --- TARJETAS CON LAS IMÁGENES REALES ---
            // IMPORTANTE: Asegúrate de que los nombres R.drawable.icono_xxx coincidan
            // con los nombres de los archivos que pegaste en la carpeta drawable.
            MenuGerenciaCard(
                idImagen = R.drawable.icono_ingresos, // El nombre de tu archivo PNG
                texto = "Ingresos\npor Destino",
                onClick = onNavigateToIngresosDestino
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuGerenciaCard(
                idImagen = R.drawable.icono_clientes, // El nombre de tu archivo PNG
                texto = "Top Clientes",
                onClick = onNavigateToTopClientes
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuGerenciaCard(
                idImagen = R.drawable.icono_paquetes, // El nombre de tu archivo PNG
                texto = "Paquetes más\nvendidos",
                onClick = onNavigateToPaquetesVendidos
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuGerenciaCard(
                idImagen = R.drawable.icono_evolution, // El nombre de tu archivo PNG
                texto = "Evolución de ventas\npor año",
                onClick = onNavigateToPaquetesVendidos
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuGerenciaCard(
                idImagen = R.drawable.icono_check_list, // El nombre de tu archivo PNG
                texto = "Promedio de evaluaciones",
                onClick = onNavigateToPaquetesVendidos
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF53A6F3)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar sesión", color = Color.Black, fontWeight = FontWeight.Medium)
                }
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Atrás", tint = Color.DarkGray, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
fun MenuGerenciaCard(idImagen: Int, texto: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- AQUÍ DIBUJAMOS LA IMAGEN DE FIGMA ---
        Image(
            painter = painterResource(id = idImagen),
            contentDescription = "Ícono de $texto",
            modifier = Modifier.size(60.dp) // Ajusta el tamaño si lo necesitas más grande o más pequeño
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = texto,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}