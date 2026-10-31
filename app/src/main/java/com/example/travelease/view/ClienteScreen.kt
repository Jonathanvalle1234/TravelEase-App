package com.example.travelease.view

import android.content.Context // 👈 Importación para SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.R
import com.example.travelease.viewmodel.ClienteViewModel

@Composable
fun ClienteScreen(
    viewModel: ClienteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToMisReservas: () -> Unit,
    onNavigateToSugerencias: () -> Unit,
    onNavigateToEncuesta: () -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    //  1. ABRIMOS LA BÓVEDA Y SACAMOS EL NOMBRE REAL DEL USUARIO
    val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
    val nombreReal = sharedPreferences.getString("NOMBRE", "Cliente") ?: "Cliente"

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- CABECERA AZUL ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF2196F3),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(vertical = 40.dp), // Un poco más de espacio arriba
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Hola, $nombreReal",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Valora tu experiencia con nosotros:", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTONES GRANDES (TARJETAS) ---
        MenuButton(
            text = "Valoraciones",
            imageResId = R.drawable.icono_valoraciones,
            onClick = onNavigateToMisReservas
        )
        Spacer(modifier = Modifier.height(24.dp))

        MenuButton(
            text = "Sugerencias",
            imageResId = R.drawable.icono_sugerencias,
            onClick = onNavigateToSugerencias
        )
        Spacer(modifier = Modifier.height(24.dp))

        MenuButton(
            text = "Encuesta de satisfacción",
            imageResId = R.drawable.icono_encuesta,
            onClick = onNavigateToEncuesta
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- BARRA INFERIOR ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {

                    viewModel.cerrarSesion(context)
                    onNavigateBack()         // Regresamos al Login
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// --- DISEÑO DE LA TARJETA XL ---
@Composable
fun MenuButton(text: String, imageResId: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(90.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = text, fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Medium)
        }
    }
}