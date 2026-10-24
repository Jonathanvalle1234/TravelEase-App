package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoEditar = Color(0xFFE9EFF8)
private val AzulEditar = Color(0xFF1E90F0)
private val BordeEditar = Color(0xFF2B2B2B)

@Composable
fun EditarReservaScreen(
    clientes: List<OpcionItem>,
    paquetes: List<OpcionItem>,
    estados: List<OpcionItem>,
    tiposPago: List<OpcionItem>,
    clienteInicialId: Int,
    paqueteInicialId: Int,
    estadoInicialId: Int,
    pagoInicialId: Int,
    onActualizar: (clienteId: Int, paqueteId: Int, estadoId: Int, tipoPagoId: Int) -> Unit,
    onLimpiar: () -> Unit,
    onCancelar: () -> Unit,
    onBackClick: () -> Unit,
    paqueteInicialNombre: String? = null,
    estadoBloqueado: Boolean = false // true cuando la reserva NO está en Pendiente
) {
    val context = LocalContext.current

    var cliente by remember { mutableStateOf<OpcionItem?>(null) }
    var paquete by remember { mutableStateOf<OpcionItem?>(null) }
    var estado by remember { mutableStateOf<OpcionItem?>(null) }
    var tipoPago by remember { mutableStateOf<OpcionItem?>(null) }

    // Precarga los valores actuales cuando llegan las listas
    LaunchedEffect(clientes) {
        if (cliente == null) cliente = clientes.firstOrNull { it.id == clienteInicialId }
    }
    LaunchedEffect(paquetes) {
        if (paquete == null) {
            paquete = paquetes.firstOrNull { it.id == paqueteInicialId }
                ?: paquetes.firstOrNull { it.nombre.equals(paqueteInicialNombre, true) }
        }
    }
    LaunchedEffect(estados) {
        if (estado == null) estado = estados.firstOrNull { it.id == estadoInicialId }
    }
    LaunchedEffect(tiposPago) {
        if (tipoPago == null) tipoPago = tiposPago.firstOrNull { it.id == pagoInicialId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoEditar)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(AzulEditar, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "EDITAR RESERVA",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp)
        ) {
            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = {
                        if (cliente == null || paquete == null || estado == null || tipoPago == null) {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_LONG).show()
                        } else {
                            onActualizar(cliente!!.id, paquete!!.id, estado!!.id, tipoPago!!.id)
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, BordeEditar),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        "Actualizar",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            CampoDropdown(
                etiqueta = "👤 CLIENTE",
                placeholder = "Seleccione un cliente",
                opciones = clientes,
                seleccion = cliente,
                onSeleccion = { cliente = it }
            )

            Spacer(Modifier.height(18.dp))

            CampoDropdown(
                etiqueta = "✈️ PAQUETE TURÍSTICO",
                placeholder = "Seleccione un paquete",
                opciones = paquetes,
                seleccion = paquete,
                onSeleccion = { paquete = it }
            )

            Spacer(Modifier.height(18.dp))

            // Estado: solo editable si la reserva está en Pendiente
            Box(Modifier.alpha(if (estadoBloqueado) 0.5f else 1f)) {
                CampoDropdown(
                    etiqueta = "📌 ESTADO DE LA RESERVA",
                    placeholder = "Seleccione un estado",
                    opciones = estados,
                    seleccion = estado,
                    onSeleccion = { estado = it },
                    habilitado = !estadoBloqueado
                )
            }
            if (estadoBloqueado) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "El estado solo se puede cambiar en reservas Pendiente",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(Modifier.height(18.dp))

            CampoDropdown(
                etiqueta = "🎫 TIPO DE PAGO",
                placeholder = "Seleccione un tipo de pago",
                opciones = tiposPago,
                seleccion = tipoPago,
                onSeleccion = { tipoPago = it }
            )

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(36.dp)
            ) {
                BotonBorde(
                    texto = "Limpiar",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        cliente = null
                        paquete = null
                        tipoPago = null
                        // El estado bloqueado no se limpia
                        if (!estadoBloqueado) estado = null
                        onLimpiar()
                    }
                )
                BotonBorde(
                    texto = "Cancelar",
                    modifier = Modifier.weight(1f),
                    onClick = onCancelar
                )
            }

            Spacer(Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 10.dp)
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onBackClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Canvas(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(width = 8.dp, height = 15.dp)
            ) {
                val grosor = 1.5.dp.toPx()
                drawLine(Color.DarkGray, Offset(size.width, 0f), Offset(0f, size.height / 2), grosor, StrokeCap.Round)
                drawLine(Color.DarkGray, Offset(0f, size.height / 2), Offset(size.width, size.height), grosor, StrokeCap.Round)
            }
        }
    }
}