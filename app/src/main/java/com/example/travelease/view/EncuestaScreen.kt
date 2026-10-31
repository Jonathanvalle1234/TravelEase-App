package com.example.travelease.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft // 👈 El icono exacto de tu imagen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.viewmodel.ClienteViewModel

@Composable
fun EncuestaScreen(viewModel: ClienteViewModel, onNavigateBack: () -> Unit) {
    // Calificaciones numéricas (1 al 5)
    var atencionAlCliente by remember { mutableStateOf(0) }
    var facilidadReserva by remember { mutableStateOf(0) }
    var relacionCalidadPrecio by remember { mutableStateOf(0) }
    var calidadServicioProporcionado by remember { mutableStateOf(0) }


    var informacionProporcionadaSencilla by remember { mutableStateOf<Int?>(null) }
    var recomendarAOtros by remember { mutableStateOf<Int?>(null) }
    var volverContratar by remember { mutableStateOf<Int?>(null) }

    // Característica favorita al final
    var caracteristicaFav by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .systemBarsPadding()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Encuesta de Satisfacción",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Por favor completa la siguiente encuesta:",
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Atención al cliente (1-5)
        PreguntaCalificacionCard(
            titulo = "Atención al cliente",
            valorSeleccionado = atencionAlCliente,
            onValorCambiado = { atencionAlCliente = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Facilidad de reserva (1-5)
        PreguntaCalificacionCard(
            titulo = "Facilidad de reserva",
            valorSeleccionado = facilidadReserva,
            onValorCambiado = { facilidadReserva = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Relación calidad - precio (1-5)
        PreguntaCalificacionCard(
            titulo = "Relación calidad - precio",
            valorSeleccionado = relacionCalidadPrecio,
            onValorCambiado = { relacionCalidadPrecio = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Calidad del servicio proporcionado (1-5)
        PreguntaCalificacionCard(
            titulo = "Calidad del servicio proporcionado",
            valorSeleccionado = calidadServicioProporcionado,
            onValorCambiado = { calidadServicioProporcionado = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Información proporcionada sencilla (Sí / No)
        PreguntaSiNoCard(
            titulo = "¿La información proporcionada fue sencilla?",
            valorSeleccionado = informacionProporcionadaSencilla,
            onSeleccion = { informacionProporcionadaSencilla = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 6. ¿Nos recomendarías a otros? (Sí / No)
        PreguntaSiNoCard(
            titulo = "¿Nos recomendarías a otros?",
            valorSeleccionado = recomendarAOtros,
            onSeleccion = { recomendarAOtros = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 7. ¿Volverías a contratar con nosotros? (Sí / No)
        PreguntaSiNoCard(
            titulo = "¿Volverías a contratar con nosotros?",
            valorSeleccionado = volverContratar,
            onSeleccion = { volverContratar = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 8. ¿Cuál fue tu característica favorita?
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "¿Cuál fue tu característica favorita?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = caracteristicaFav,
                    onValueChange = { caracteristicaFav = it },
                    placeholder = { Text("Ej. Buen catálogo.") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN ENVIAR (Vuelve a ser de ancho completo) ---
        Button(
            onClick = {
                viewModel.enviarEncuesta(
                    atencionAlCliente = atencionAlCliente,
                    facilidadReserva = facilidadReserva,
                    relacionCalidadPrecio = relacionCalidadPrecio,
                    calidadServicioProporcionado = calidadServicioProporcionado,
                    caracteristicaFav = caracteristicaFav,
                    informacionProporcionadaSencilla = informacionProporcionadaSencilla ?: 0,
                    recomendarAOtros = recomendarAOtros ?: 0,
                    volverContratar = volverContratar ?: 0,
                    onSuccess = {
                        android.widget.Toast.makeText(context, "¡Encuesta guardada con éxito!", android.widget.Toast.LENGTH_SHORT).show()
                        onNavigateBack()
                    },
                    onError = { errorMsg ->
                        android.util.Log.d("MOTIVO", "🚨 MOTIVO EXACTO: $errorMsg")
                        android.widget.Toast.makeText(context, "Error al enviar encuesta", android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Enviar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))


        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart // La empuja totalmente a la izquierda
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Regresar",
                    tint = Color.Gray, // Un tono sutil como en tu imagen
                    modifier = Modifier.size(32.dp) // Tamaño ligeramente más grande
                )
            }
        }


        Spacer(modifier = Modifier.height(250.dp))
    }
}

// Tarjeta para calificaciones de 1 a 5 con círculos acumulativos amarillos
@Composable
fun PreguntaCalificacionCard(
    titulo: String,
    valorSeleccionado: Int,
    onValorCambiado: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..5) {
                    val esActivo = i <= valorSeleccionado
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onValorCambiado(i) },
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.size(26.dp)) {
                            drawCircle(
                                color = if (esActivo) Color(0xFFFFC107) else Color.Gray,
                                radius = size.minDimension / 2,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
                            )
                        }
                        if (esActivo) {
                            androidx.compose.foundation.Canvas(modifier = Modifier.size(14.dp)) {
                                drawCircle(
                                    color = Color(0xFFFFC107),
                                    radius = size.minDimension / 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta para preguntas de Sí / No (Sí = 1, No = 0)
@Composable
fun PreguntaSiNoCard(
    titulo: String,
    valorSeleccionado: Int?,
    onSeleccion: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSeleccion(1) }
                ) {
                    RadioSimple(seleccionado = valorSeleccionado == 1)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sí", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSeleccion(0) }
                ) {
                    RadioSimple(seleccionado = valorSeleccionado == 0)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "No", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun RadioSimple(seleccionado: Boolean) {
    Box(
        modifier = Modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(24.dp)) {
            drawCircle(
                color = if (seleccionado) Color(0xFFFFC107) else Color.Gray,
                radius = size.minDimension / 2,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
            )
        }
        if (seleccionado) {
            androidx.compose.foundation.Canvas(modifier = Modifier.size(14.dp)) {
                drawCircle(
                    color = Color(0xFFFFC107),
                    radius = size.minDimension / 2
                )
            }
        }
    }
}