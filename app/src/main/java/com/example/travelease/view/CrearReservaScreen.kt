package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

private val FondoPantalla = Color(0xFFE9EFF8)
private val AzulHeader = Color(0xFF1E90F0)
private val BordeOscuro = Color(0xFF2B2B2B)

/** Opción genérica para los desplegables (cliente, paquete, estado, tipo de pago) */
data class OpcionItem(val id: Int, val nombre: String)

@Composable
fun CrearReservaScreen(
    clientes: List<OpcionItem>,
    paquetes: List<OpcionItem>,
    estados: List<OpcionItem>,
    tiposPago: List<OpcionItem>,
    onAgregar: (clienteId: Int, paqueteId: Int, estadoId: Int, tipoPagoId: Int) -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current

    // Valores por defecto como en Figma: Pendiente y Efectivo
    fun estadoInicial() = estados.firstOrNull { it.nombre.equals("Pendiente", true) } ?: estados.firstOrNull()
    fun pagoInicial() = tiposPago.firstOrNull { it.nombre.equals("Efectivo", true) } ?: tiposPago.firstOrNull()

    var cliente by remember { mutableStateOf<OpcionItem?>(null) }
    var paquete by remember { mutableStateOf<OpcionItem?>(null) }
    var estado by remember(estados) { mutableStateOf(estadoInicial()) }
    var tipoPago by remember(tiposPago) { mutableStateOf(pagoInicial()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        // Header azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(AzulHeader, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "CREAR RESERVA",
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

            // Botón Agregar (siempre activo, avisa si falta algo)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = {
                        if (cliente == null || paquete == null || estado == null || tipoPago == null) {
                            Toast.makeText(
                                context,
                                "Selecciona cliente y paquete (si no hay opciones, revisa la conexión)",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            onAgregar(cliente!!.id, paquete!!.id, estado!!.id, tipoPago!!.id)
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, BordeOscuro),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        "Agregar",
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

            CampoDropdown(
                etiqueta = "📌 ESTADO DE LA RESERVA",
                placeholder = "Seleccione un estado",
                opciones = estados,
                seleccion = estado,
                onSeleccion = { estado = it }
            )

            Spacer(Modifier.height(18.dp))

            CampoDropdown(
                etiqueta = "🎫 TIPO DE PAGO",
                placeholder = "Seleccione un tipo de pago",
                opciones = tiposPago,
                seleccion = tipoPago,
                onSeleccion = { tipoPago = it }
            )

            Spacer(Modifier.height(28.dp))

            // Botones Limpiar / Cancelar
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
                        estado = estadoInicial()
                        tipoPago = pagoInicial()
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

        // Flecha de regreso abajo a la izquierda
        Box(
            modifier = Modifier
                .padding(start = 14.dp, top = 6.dp, bottom = 10.dp)
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onCancelar() },
            contentAlignment = Alignment.CenterStart
        ) {
            Canvas(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(width = 8.dp, height = 15.dp)
            ) {
                val grosor = 1.5.dp.toPx()
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(size.width, 0f),
                    end = Offset(0f, size.height / 2),
                    strokeWidth = grosor,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height),
                    strokeWidth = grosor,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
internal fun CampoDropdown(
    etiqueta: String,
    placeholder: String,
    opciones: List<OpcionItem>,
    seleccion: OpcionItem?,
    onSeleccion: (OpcionItem) -> Unit,
    habilitado: Boolean = true
) {
    var expandido by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(8.dp)

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.Black
        )
        Spacer(Modifier.height(6.dp))

        Box(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.White, forma)
                    .border(
                        width = if (expandido) 2.dp else 1.dp,
                        color = if (expandido) AzulHeader else BordeOscuro,
                        shape = forma
                    )
                    .clickable(enabled = habilitado) { expandido = true }
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = seleccion?.nombre ?: placeholder,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.Black,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                if (opciones.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Sin opciones", color = Color.Gray) },
                        onClick = { expandido = false },
                        enabled = false
                    )
                } else {
                    opciones.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.nombre) },
                            onClick = {
                                onSeleccion(opcion)
                                expandido = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun BotonBorde(
    texto: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BordeOscuro),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = modifier.height(36.dp)
    ) {
        Text(
            text = texto,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}