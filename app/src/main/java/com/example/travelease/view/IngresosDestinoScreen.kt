package com.example.travelease.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IngresosDestinoScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Ingresos por Destino", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Compara los destinos que generan más ingresos", fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(24.dp))

        // Tarjetas en formato de cuadrícula (2 columnas)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DestinoCard(modifier = Modifier.weight(1f), ciudad = "Ciudad de México", monto = "USD 9,425.00", porcentaje = "22.4 % del total")
            DestinoCard(modifier = Modifier.weight(1f), ciudad = "Miami", monto = "USD 9,425.00", porcentaje = "22.4 % del total")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DestinoCard(modifier = Modifier.weight(1f), ciudad = "Los Ángeles", monto = "USD 6,400.00", porcentaje = "15.2 % del total")
            DestinoCard(modifier = Modifier.weight(1f), ciudad = "Madrid", monto = "USD 4,410.00", porcentaje = "10.5 % del total")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DestinoCard(modifier = Modifier.weight(1f), ciudad = "Toronto", monto = "USD 1,250.00", porcentaje = "3.0 % del total")
            DestinoCard(modifier = Modifier.weight(1f), ciudad = "Bogotá", monto = "USD 1,250.00", porcentaje = "3.0 % del total")
        }

        Spacer(modifier = Modifier.weight(1f))


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Atrás", tint = Color.Gray, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun DestinoCard(modifier: Modifier = Modifier, ciudad: String, monto: String, porcentaje: String) {
    Column(
        modifier = modifier.border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)).background(Color.White).padding(12.dp)
    ) {
        Text(text = ciudad, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = monto, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = porcentaje, fontSize = 10.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color(0xFF2196F3), RoundedCornerShape(2.dp)))
    }
}