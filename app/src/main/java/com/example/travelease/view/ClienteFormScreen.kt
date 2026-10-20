package com.example.travelease.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelease.viewmodel.ClienteViewModel

// Modelos locales para la jerarquía en cascada
data class MunicipioModel(val id: Int, val nombre: String)
data class DepartamentoModel(val id: Int, val nombre: String, val municipios: List<MunicipioModel>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteFormScreen(
    modo: String,
    clienteId: Int = 0,
    viewModel: ClienteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val esCrear = modo.trim().lowercase() == "crear"
    val tituloPantalla = if (esCrear) "Nuevo Cliente" else "Editar Cliente"
    val textoBoton = if (esCrear) "Agregar" else "Actualizar"

    // Campos de texto normales
    var nombre by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    // --- LISTA OFICIAL COMPLETA DE DEPARTAMENTOS Y MUNICIPIOS DE NICARAGUA ---
    val listaDepartamentos = remember {
        listOf(
            DepartamentoModel(id = 1, nombre = "Managua", municipios = listOf(MunicipioModel(1, "Managua"), MunicipioModel(2, "Tipitapa"), MunicipioModel(3, "Ciudad Sandino"), MunicipioModel(4, "Mateare"), MunicipioModel(5, "San Rafael del Sur"), MunicipioModel(6, "Villa El Carmen"), MunicipioModel(7, "El Crucero"))),
            DepartamentoModel(id = 2, nombre = "León", municipios = listOf(MunicipioModel(8, "León"), MunicipioModel(9, "Nagarote"), MunicipioModel(10, "La Paz Centro"), MunicipioModel(11, "Telica"), MunicipioModel(12, "Quezalguaque"), MunicipioModel(13, "El Jicaral"), MunicipioModel(14, "Achuapa"), MunicipioModel(15, "Santa Rosa del Peñón"), MunicipioModel(16, "El Sauce"))),
            DepartamentoModel(id = 3, nombre = "Chinandega", municipios = listOf(MunicipioModel(17, "Chinandega"), MunicipioModel(18, "El Realejo"), MunicipioModel(19, "Corinto"), MunicipioModel(20, "Chichigalpa"), MunicipioModel(21, "Posoltega"), MunicipioModel(22, "El Viejo"), MunicipioModel(23, "Puerto Morazán"), MunicipioModel(24, "Somotillo"), MunicipioModel(25, "Villa Nueva"), MunicipioModel(26, "Santo Tomás del Norte"), MunicipioModel(27, "Cinco Pinos"), MunicipioModel(28, "San Francisco del Norte"))),
            DepartamentoModel(id = 4, nombre = "Masaya", municipios = listOf(MunicipioModel(29, "Masaya"), MunicipioModel(30, "Nindirí"), MunicipioModel(31, "Ticuantepe"), MunicipioModel(32, "Niquinohomo"), MunicipioModel(33, "Catarina"), MunicipioModel(34, "San Juan de Oriente"), MunicipioModel(35, "La Concepción"), MunicipioModel(36, "Masatepe"))),
            DepartamentoModel(id = 5, nombre = "Granada", municipios = listOf(MunicipioModel(37, "Granada"), MunicipioModel(38, "Nandaime"), MunicipioModel(39, "Diriá"), MunicipioModel(40, "Diriomo"))),
            DepartamentoModel(id = 6, nombre = "Carazo", municipios = listOf(MunicipioModel(41, "Jinotepe"), MunicipioModel(42, "Diriamba"), MunicipioModel(43, "San Marcos"), MunicipioModel(44, "Dolores"), MunicipioModel(45, "La Conquista"), MunicipioModel(46, "El Rosario"), MunicipioModel(47, "Santa Teresa"))),
            DepartamentoModel(id = 7, nombre = "Rivas", municipios = listOf(MunicipioModel(48, "Rivas"), MunicipioModel(49, "San Jorge"), MunicipioModel(50, "Buenos Aires"), MunicipioModel(51, "Potosí"), MunicipioModel(52, "Belén"), MunicipioModel(53, "Tola"), MunicipioModel(54, "San Juan del Sur"), MunicipioModel(55, "Cárdenas"))),
            DepartamentoModel(id = 8, nombre = "Nueva Segovia", municipios = listOf(MunicipioModel(56, "Ocotal"), MunicipioModel(57, "Santa María"), MunicipioModel(58, "Macuelizo"), MunicipioModel(59, "Dipilto"), MunicipioModel(60, "Ciudad Antigua"), MunicipioModel(61, "San Fernando"), MunicipioModel(62, "El Jícaro"), MunicipioModel(63, "Murra"), MunicipioModel(64, "Quilalí"), MunicipioModel(65, "Wiwilí de Nueva Segovia"))),
            DepartamentoModel(id = 9, nombre = "Madriz", municipios = listOf(MunicipioModel(66, "Somoto"), MunicipioModel(67, "San Lucas"), MunicipioModel(68, "Las Sabanas"), MunicipioModel(69, "San José de Cusmapa"), MunicipioModel(70, "Telpaneca"), MunicipioModel(71, "San Juan de Río Coco"), MunicipioModel(72, "Yalagüina"))),
            DepartamentoModel(id = 10, nombre = "Estelí", municipios = listOf(MunicipioModel(73, "Estelí"), MunicipioModel(74, "Pueblo Nuevo"), MunicipioModel(75, "Condega"), MunicipioModel(76, "San Juan de Limay"), MunicipioModel(77, "La Trinidad"))),
            DepartamentoModel(id = 11, nombre = "Jinotega", municipios = listOf(MunicipioModel(78, "Jinotega"), MunicipioModel(79, "San Rafael del Norte"), MunicipioModel(80, "San Sebastián de Yalí"), MunicipioModel(81, "La Concordia"), MunicipioModel(82, "Santa María de Pantasma"), MunicipioModel(83, "El Cuá"), MunicipioModel(84, "Wiwilí de Jinotega"))),
            DepartamentoModel(id = 12, nombre = "Matagalpa", municipios = listOf(MunicipioModel(85, "Matagalpa"), MunicipioModel(86, "San Ramón"), MunicipioModel(87, "Sébaco"), MunicipioModel(88, "Ciudad Darío"), MunicipioModel(89, "Terrabona"), MunicipioModel(90, "Esquipulas"), MunicipioModel(91, "San Dionisio"), MunicipioModel(92, "San Isidro"), MunicipioModel(93, "Muy Muy"), MunicipioModel(94, "Matiguás"), MunicipioModel(95, "Río Blanco"))),
            DepartamentoModel(id = 13, nombre = "Boaco", municipios = listOf(MunicipioModel(96, "Boaco"), MunicipioModel(97, "San José de los Remates"), MunicipioModel(98, "San Lorenzo"), MunicipioModel(99, "Teustepe"), MunicipioModel(100, "Camoapa"), MunicipioModel(101, "Santa Lucía"))),
            DepartamentoModel(id = 14, nombre = "Chontales", municipios = listOf(MunicipioModel(102, "Juigalpa"), MunicipioModel(103, "Acoyapa"), MunicipioModel(104, "Comalapa"), MunicipioModel(105, "El Coral"), MunicipioModel(106, "La Libertad"), MunicipioModel(107, "San Francisco de Cuapa"), MunicipioModel(108, "San Pedro de Lóvago"), MunicipioModel(109, "Santo Domingo"), MunicipioModel(110, "Santo Tomás"))),
            DepartamentoModel(id = 15, nombre = "Río San Juan", municipios = listOf(MunicipioModel(111, "San Carlos"), MunicipioModel(112, "El Almendro"), MunicipioModel(113, "Morrito"), MunicipioModel(114, "San Miguelito"), MunicipioModel(115, "El Castillo"), MunicipioModel(116, "San Juan de Nicaragua"))),
            DepartamentoModel(id = 16, nombre = "Región Autónoma de la Costa Caribe Norte", municipios = listOf(MunicipioModel(117, "Puerto Cabezas (Bilwi)"), MunicipioModel(118, "Waspam"), MunicipioModel(119, "Prinzapolka"), MunicipioModel(120, "Rosita"), MunicipioModel(121, "Siuna"), MunicipioModel(122, "Bonanza"), MunicipioModel(123, "Mulukukú"), MunicipioModel(124, "Waslala"))),
            DepartamentoModel(id = 17, nombre = "Región Autónoma de la Costa Caribe Sur", municipios = listOf(MunicipioModel(125, "Bluefields"), MunicipioModel(126, "El Rama"), MunicipioModel(127, "Kukra Hill"), MunicipioModel(128, "La Cruz de Río Grande"), MunicipioModel(129, "Laguna de Perlas"), MunicipioModel(130, "Muelle de los Bueyes"), MunicipioModel(131, "Nueva Guinea"), MunicipioModel(132, "Corn Island"), MunicipioModel(133, "El Tortuguero"), MunicipioModel(134, "Bocana de Paiwas")))
        )
    }

    var departamentoSeleccionado by remember { mutableStateOf<DepartamentoModel?>(null) }
    var municipiosDisponibles by remember { mutableStateOf<List<MunicipioModel>>(emptyList()) }
    var municipioSeleccionado by remember { mutableStateOf<MunicipioModel?>(null) }

    var expandedDep by remember { mutableStateOf(false) }
    var expandedMun by remember { mutableStateOf(false) }

    val listaClientes by viewModel.listaClientes.collectAsState()

    //  CONTROL: Para saber si ya llenamos los datos y no borrar tus ediciones
    var datosLlenados by remember { mutableStateOf(false) }

    //  MAGIA CORREGIDA: Escuchamos cambios en la lista de clientes
    LaunchedEffect(clienteId, listaClientes) {
        if (!esCrear && clienteId > 0 && !datosLlenados) {

            // 1. Si la memoria está vacía en esta pantalla, pedimos los clientes a Django
            if (listaClientes.isEmpty()) {
                viewModel.cargarClientes()
            }
            // 2. Si ya llegaron los clientes, buscamos al nuestro y llenamos todo
            else {
                val clienteAEditar = listaClientes.find { it.id == clienteId }

                if (clienteAEditar != null) {
                    nombre = clienteAEditar.name ?: ""
                    cedula = clienteAEditar.nationalId ?: ""
                    correo = clienteAEditar.userEmail ?: ""
                    direccion = clienteAEditar.address ?: ""
                    telefono = clienteAEditar.phoneNumber ?: ""

                    val munIdStr = clienteAEditar.municipality?.toString() ?: ""
                    if (munIdStr.isNotEmpty()) {
                        val munIdInt = munIdStr.toIntOrNull()
                        if (munIdInt != null) {
                            val depEncontrado = listaDepartamentos.find { dep ->
                                dep.municipios.any { mun -> mun.id == munIdInt }
                            }
                            if (depEncontrado != null) {
                                departamentoSeleccionado = depEncontrado
                                municipiosDisponibles = depEncontrado.municipios
                                municipioSeleccionado = depEncontrado.municipios.find { it.id == munIdInt }
                            }
                        }
                    }
                    // Bloqueamos para que no se vuelva a cargar y te borre algo mientras editas
                    datosLlenados = true
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF3F4F6)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF2196F3),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = tituloPantalla,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "UBICACIÓN Y DATOS DEL CLIENTE", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedDep,
                    onExpandedChange = { expandedDep = !expandedDep }
                ) {
                    OutlinedTextField(
                        value = departamentoSeleccionado?.nombre ?: "Seleccione un departamento",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Departamento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDep) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDep,
                        onDismissRequest = { expandedDep = false }
                    ) {
                        listaDepartamentos.forEach { dep ->
                            DropdownMenuItem(
                                text = { Text(dep.nombre) },
                                onClick = {
                                    departamentoSeleccionado = dep
                                    municipiosDisponibles = dep.municipios
                                    municipioSeleccionado = dep.municipios.firstOrNull()
                                    expandedDep = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedMun,
                    onExpandedChange = { if (departamentoSeleccionado != null) expandedMun = !expandedMun }
                ) {
                    OutlinedTextField(
                        value = municipioSeleccionado?.nombre ?: "Seleccione un municipio",
                        onValueChange = {},
                        readOnly = true,
                        enabled = departamentoSeleccionado != null,
                        label = { Text("Municipio") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMun) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMun,
                        onDismissRequest = { expandedMun = false }
                    ) {
                        municipiosDisponibles.forEach { mun ->
                            DropdownMenuItem(
                                text = { Text(mun.nombre) },
                                onClick = {
                                    municipioSeleccionado = mun
                                    expandedMun = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    label = { Text("Nombre completo del cliente") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cedula, onValueChange = { cedula = it },
                    label = { Text("Cédula o DNI") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = correo, onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = esCrear // Correo no se edita
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = direccion, onValueChange = { direccion = it },
                    label = { Text("Dirección exacta") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = telefono, onValueChange = { telefono = it },
                    label = { Text("Número de teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (nombre.isBlank() || cedula.isBlank()) {
                                Toast.makeText(context, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val depIdStr = departamentoSeleccionado?.id?.toString() ?: "1"
                            val munIdStr = municipioSeleccionado?.id?.toString() ?: "1"

                            if (esCrear) {
                                viewModel.crearCliente(
                                    nombre = nombre, cedula = cedula, correo = correo,
                                    direccion = direccion, telefono = telefono,
                                    departamentoStr = depIdStr, municipioStr = munIdStr,
                                    onSuccess = {
                                        Toast.makeText(context, "¡Cliente registrado con éxito!", Toast.LENGTH_SHORT).show()
                                        onNavigateBack()
                                    },
                                    onError = { mensajeError ->
                                        Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show()
                                    }
                                )
                            } else {
                                viewModel.actualizarCliente(
                                    clienteId = clienteId,
                                    nombre = nombre,
                                    cedula = cedula,
//                                    correo = correo, // 🚀 ¡AQUÍ MANDAMOS EL CORREO!
                                    direccion = direccion,
                                    telefono = telefono,
                                    departamentoStr = depIdStr,
                                    municipioStr = munIdStr,
                                    onSuccess = {
                                        Toast.makeText(context, "¡Cliente actualizado con éxito!", Toast.LENGTH_SHORT).show()
                                        onNavigateBack()
                                    },
                                    onError = { mensajeError ->
                                        Toast.makeText(context, "Error al actualizar: $mensajeError", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text(textoBoton)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    OutlinedButton(
                        onClick = {
                            nombre = ""
                            cedula = ""
                            if (esCrear) correo = ""
                            direccion = ""
                            telefono = ""
                            departamentoSeleccionado = null
                            municipioSeleccionado = null
                            municipiosDisponibles = emptyList()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Limpiar", color = Color.Black)
                    }
                }
            }
        }
    }
}