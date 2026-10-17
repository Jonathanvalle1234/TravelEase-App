package com.example.travelease.view

import android.widget.Toast // 🔥 Importante para el mensaje flotante
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.viewmodel.HotelViewModel

@Composable
fun HotelFormScreen(
    viewModel: HotelViewModel,
    hotelId: Int?,
    onNavigateBack: () -> Unit
) {
    val hoteles by viewModel.hoteles.collectAsState()
    val hotelAEditar = hoteles.find { it.id == hotelId }
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(hotelAEditar?.nombre ?: "") }
    var ciudad by remember { mutableStateOf(hotelAEditar?.ciudad ?: "") }
    var estrellas by remember { mutableStateOf(hotelAEditar?.estrellas?.toString() ?: "") }
    var precio by remember { mutableStateOf(hotelAEditar?.precioActual?.toString() ?: "") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val titulo = if (hotelId == null) "DATOS DEL HOTEL" else "EDITAR HOTEL"
    val textoBotonPrincipal = if (hotelId == null) "Agregar" else "Actualizar"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
            .imePadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
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
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = {
                        if (nombre.isNotBlank() && ciudad.isNotBlank() && precio.isNotBlank() && estrellas.isNotBlank()) {
                            isLoading = true
                            viewModel.guardarHotel(
                                context = context,
                                hotelId = hotelId, nombre = nombre, ciudad = ciudad,
                                estrellas = estrellas.toIntOrNull() ?: 1, precioStr = precio,
                                onSuccess = {
                                    isLoading = false
                                    // 🔥 MAGIA AQUÍ: Mostramos el mensaje de éxito antes de regresar
                                    val mensajeExito = if (hotelId == null) "Hotel agregado con éxito" else "Hotel actualizado con éxito"
                                    Toast.makeText(context, mensajeExito, Toast.LENGTH_SHORT).show()

                                    onNavigateBack() // Regresamos a la lista
                                },
                                onError = { error -> isLoading = false; errorMessage = error }
                            )
                        } else { errorMessage = "Completa todos los campos" }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                    } else {
                        Text(textoBotonPrincipal, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontWeight = FontWeight.Medium, modifier = Modifier.padding(vertical = 8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            FigmaTextField(label = "Nombre del hotel", value = nombre, onValueChange = { nombre = it })
            FigmaTextField(label = "Ciudad", value = ciudad, onValueChange = { ciudad = it })
            FigmaTextField(label = "Precio", value = precio, onValueChange = { precio = it }, isNumber = true)
            FigmaTextField(label = "Estrellas", value = estrellas, onValueChange = { estrellas = it }, isNumber = true)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { nombre = ""; ciudad = ""; precio = ""; estrellas = "" },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("limpiar", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FigmaTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isNumber: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}