package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelease.viewmodel.VueloViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun VueloFormScreen(
    modo: String,
    vueloId: Int,
    viewModel: VueloViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var aerolinea by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fechaSalida by remember { mutableStateOf("") }
    var fechaEntrada by remember { mutableStateOf("") }

    // 🚀 CARGAR DATOS AUTOMÁTICAMENTE AL EDITAR
    LaunchedEffect(key1 = vueloId) {
        if (modo == "editar" && vueloId > 0) {
            viewModel.obtenerVueloPorId(vueloId) { vuelo ->
                aerolinea = vuelo.aerolinea
                precio = vuelo.precio.toString()
                origen = vuelo.origen
                destino = vuelo.destino
                fechaSalida = vuelo.fechaSalida
                fechaEntrada = vuelo.fechaEntrada
            }
        }
    }

    // 🚀 Función directa para enviar los datos al ViewModel
    val ejecutarGuardado = {
        if (aerolinea.isBlank() || precio.isBlank() || origen.isBlank() || destino.isBlank()) {
            Toast.makeText(context, "Por favor, llene los campos principales", Toast.LENGTH_SHORT).show()
        } else {
            println("🚀 ENVIANDO A DJANGO -> Aerolínea: '$aerolinea', Precio: '$precio', Origen: '$origen', Salida: '$fechaSalida'")

            viewModel.guardarVuelo(
                modo = modo,
                vueloId = vueloId,
                aerolinea = aerolinea,
                precioStr = precio,
                origen = origen,
                destino = destino,
                fechaSalida = fechaSalida,
                fechaEntrada = fechaEntrada,
                onSuccess = {
                    Toast.makeText(context, "¡Vuelo guardado con éxito!", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                onError = { errorMensaje ->
                    Toast.makeText(context, "Django dice: $errorMensaje", Toast.LENGTH_LONG).show()
                    println("🚨 Error de Django al guardar: $errorMensaje")
                }
            )
        }
    }

    // 🚀 Configuración del Scroll State
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F5))
            .verticalScroll(scrollState)
            .imePadding() // Empuja el contenido cuando el teclado sube
            .padding(16.dp)
    ) {
        // --- Cabecera Azul ---
        Surface(
            color = Color(0xFF2196F3),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (modo == "editar") "EDITAR VUELO" else "DATOS DEL VUELO",
                    color = Color.White,
                    fontSize = 20.sp, // Ajustado para coincidir con Transporte
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- Botón principal alineado a la derecha ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FormOutlinedButton(
                text = if (modo == "editar") "Actualizar" else "Agregar",
                onClick = ejecutarGuardado
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Campos de texto del formulario ---
        Text(text = "Aerolínea", fontWeight = FontWeight.Bold, color = Color.Black)
        VueloTextField(
            value = aerolinea,
            onValueChange = { aerolinea = it },
            placeholder = "Ej. Avianca",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Precio", fontWeight = FontWeight.Bold, color = Color.Black)
        VueloTextField(
            value = precio,
            onValueChange = { precio = it },
            placeholder = "Ej. 250.00",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Origen", fontWeight = FontWeight.Bold, color = Color.Black)
                VueloTextField(
                    value = origen,
                    onValueChange = { origen = it },
                    placeholder = "Nicaragua",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Destino", fontWeight = FontWeight.Bold, color = Color.Black)
                VueloTextField(
                    value = destino,
                    onValueChange = { destino = it },
                    placeholder = "Costa Rica",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campos de Fecha
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Fecha salida", fontWeight = FontWeight.Bold, color = Color.Black)
                VueloDateField(
                    value = fechaSalida,
                    onDateSelected = { fechaSalida = it },
                    placeholder = "dd/mm/aaaa"
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Fecha entrada", fontWeight = FontWeight.Bold, color = Color.Black)
                VueloDateField(
                    value = fechaEntrada,
                    onDateSelected = { fechaEntrada = it },
                    placeholder = "dd/mm/aaaa"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Botones Secundarios (Limpiar y Cancelar) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FormOutlinedButton(
                text = "limpiar",
                onClick = {
                    aerolinea = ""; precio = ""; origen = ""; destino = ""; fechaSalida = ""; fechaEntrada = ""
                },
                modifier = Modifier.width(130.dp)
            )

            FormOutlinedButton(
                text = "Cancelar",
                onClick = onNavigateBack,
                modifier = Modifier.width(130.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Botón de Retroceso Inferior ---
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Regresar",
                modifier = Modifier.size(36.dp),
                tint = Color.DarkGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VueloDateField(
    value: String,
    onDateSelected: (String) -> Unit,
    placeholder: String
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
            .height(52.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = Color.Black
                )
            }
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable { showDialog = true }
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        onDateSelected(sdf.format(Date(millis)))
                    }
                }) {
                    Text("Aceptar", fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun VueloTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true
    )
}

//  Modificamos el botón para aceptar anchos dinámicos (modifier)
@Composable
fun FormOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}