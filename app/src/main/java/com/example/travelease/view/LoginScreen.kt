package com.example.travelease.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.R
import com.example.travelease.viewmodel.ClienteViewModel

@Composable
fun LoginScreen(
    viewModel: ClienteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToCliente: () -> Unit,
    onNavigateToGerencia: () -> Unit,
    onNavigateToAsistente: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val mensajeRespuesta = viewModel.loginMessage.value

    // Obtenemos el contexto de Android para que el ViewModel guarde el token
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_login_2),
            contentDescription = "Logo TravelEase",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenido a\nTravelEase",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.iniciarSesion(
                    context = context,
                    correo = usuario,
                    clave = contrasena,
                    // AHORA RECIBE EL ROL DIRECTAMENTE DEL VIEWMODEL
                    onSuccess = { rolRecibido ->

                        // PASO 1: Limpiamos las cajas de texto dejándolas vacías
                        usuario = ""
                        contrasena = ""

                        // PASO 2: Limpiamos el mensaje rojo de éxito/error del ViewModel
                        viewModel.limpiarMensaje()

                        // 🚀 PASO 3: Navegamos asegurando todas las variantes de Gerencia
                        when (rolRecibido.lowercase()) {
                            "admin", "gerencia", "gerente" -> onNavigateToGerencia()
                            "asistente" -> onNavigateToAsistente()
                            else -> onNavigateToCliente() // Por defecto si es cliente
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Iniciar sesión", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(text = mensajeRespuesta, color = Color.Red, modifier = Modifier.padding(top = 16.dp))

        Spacer(modifier = Modifier.height(40.dp))
    }
}