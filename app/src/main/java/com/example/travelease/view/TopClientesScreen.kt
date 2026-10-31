package com.example.travelease.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopClientesScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Top Clientes", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Muestra los clientes con mayor monto comprado en reservas turísticas", fontSize = 14.sp, textAlign = TextAlign.Center, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.background(Color(0xFF1976D2), RoundedCornerShape(16.dp)).padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(text = "Total: USD 4,790.00", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(48.dp)) // Espacio fijo moderado para separar el gráfico del botón


        Row(
            modifier = Modifier.fillMaxWidth().height(250.dp).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            BarraGrafico(altura = 80.dp, color = Color(0xFFBBDEFB), rank = "#4", nombre = "Cecilia\nMontoya")
            BarraGrafico(altura = 120.dp, color = Color(0xFF90CAF9), rank = "#3", nombre = "Luis\nGarcía")
            BarraGrafico(altura = 170.dp, color = Color(0xFF64B5F6), rank = "#2", nombre = "Andrés\nHerrera")
            BarraGrafico(altura = 220.dp, color = Color(0xFF2196F3), rank = "#1", nombre = "Nora\nGuzmán")
        }

        Spacer(modifier = Modifier.weight(1f)) // <--- EL RESORTE GIGANTE: Ahora solo empuja la flecha hacia abajo


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Atrás", tint = Color.Gray, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun BarraGrafico(altura: androidx.compose.ui.unit.Dp, color: Color, rank: String, nombre: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.width(50.dp).height(altura).background(color, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = rank, color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = nombre, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}