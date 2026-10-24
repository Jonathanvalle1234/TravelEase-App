package com.example.travelease.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Pantallas
import com.example.travelease.view.LoginScreen
import com.example.travelease.view.ClienteScreen
import com.example.travelease.view.GerenciaScreen
import com.example.travelease.view.MisReservasScreen
import com.example.travelease.view.ValoracionesScreen
import com.example.travelease.view.SugerenciasScreen
import com.example.travelease.view.EncuestaScreen
import com.example.travelease.view.PaquetesVendidosScreen
import com.example.travelease.view.IngresosDestinoScreen
import com.example.travelease.view.TopClientesScreen
import com.example.travelease.viewmodel.ClienteViewModel

import com.example.travelease.view.MenuAsistenteScreen
import com.example.travelease.view.ClientesListScreen
import com.example.travelease.view.ClienteFormScreen
import com.example.travelease.view.ServiciosMenuScreen

// Vuelos
import com.example.travelease.view.VuelosListScreen
import com.example.travelease.view.VueloFormScreen

// Transporte
import com.example.travelease.view.TransportesListScreen
import com.example.travelease.view.TransporteFormScreen

// Hoteles
import com.example.travelease.view.HotelesListScreen
import com.example.travelease.view.HotelFormScreen
import com.example.travelease.viewmodel.HotelViewModel

// Reservas
import com.example.travelease.view.ReservaPanelScreen
import com.example.travelease.view.CrearReservaScreen
import com.example.travelease.view.EditarReservaScreen
import com.example.travelease.model.ReservaRequest
import com.example.travelease.viewmodel.ReservaPanelViewModel

sealed class RutasAsistente(val ruta: String) {
    object MenuAsistente : RutasAsistente("menu_asistente")
    object ListaClientes : RutasAsistente("lista_clientes")
    object ServiciosMenu : RutasAsistente("servicios_menu")
    object PanelReservas : RutasAsistente("panel_reservas")
    object CrearReserva : RutasAsistente("crear_reserva")

    object EditarReserva : RutasAsistente("editar_reserva/{id}") {
        fun crearRuta(id: Int) = "editar_reserva/$id"
    }

    object FormularioCliente : RutasAsistente("formulario_cliente/{modo}/{id}") {
        fun crearRuta(modo: String, id: Int = 0) = "formulario_cliente/$modo/$id"
    }

    object ListaVuelos : RutasAsistente("lista_vuelos")

    object FormularioVuelo : RutasAsistente("formulario_vuelo/{modo}/{id}") {
        fun crearRuta(modo: String, id: Int = 0) = "formulario_vuelo/$modo/$id"
    }

    object ListaTransportes : RutasAsistente("lista_transportes")

    object FormularioTransporte : RutasAsistente("formulario_transporte/{modo}/{id}") {
        fun crearRuta(modo: String, id: Int = 0) = "formulario_transporte/$modo/$id"
    }

    object ListaHoteles : RutasAsistente("lista_hoteles")

    object FormularioHotel : RutasAsistente("formulario_hotel/{modo}/{id}") {
        fun crearRuta(modo: String, id: Int = 0) = "formulario_hotel/$modo/$id"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val transporteViewModel: com.example.travelease.viewmodel.TransporteViewModel = viewModel()
    val hotelViewModel: HotelViewModel = viewModel()
    val reservaPanelViewModel: ReservaPanelViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        // --- 1. LOGIN ---
        composable("login") {
            LoginScreen(
                onNavigateToCliente = { navController.navigate("cliente") },
                onNavigateToGerencia = { navController.navigate("gerencia") },
                onNavigateToAsistente = { navController.navigate(RutasAsistente.MenuAsistente.ruta) }
            )
        }

        // --- 2. CLIENTE ---
        composable("cliente") {
            ClienteScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMisReservas = { navController.navigate("mis_reservas") },
                onNavigateToSugerencias = { navController.navigate("sugerencias") },
                onNavigateToEncuesta = { navController.navigate("encuesta") }
            )
        }

        composable(route = "mis_reservas") {
            MisReservasScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEvaluar = { idReserva, paquete, factura, total ->
                    navController.navigate(route = "valoraciones/$idReserva/$paquete/$factura/$total")
                }
            )
        }

        composable(
            route = "valoraciones/{idReserva}/{paquete}/{factura}/{total}",
            arguments = listOf(
                navArgument(name = "idReserva") { type = NavType.StringType },
                navArgument(name = "paquete") { type = NavType.StringType },
                navArgument(name = "factura") { type = NavType.StringType },
                navArgument(name = "total") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idReserva = backStackEntry.arguments?.getString("idReserva") ?: ""
            val paquete = backStackEntry.arguments?.getString("paquete") ?: ""
            val factura = backStackEntry.arguments?.getString("factura") ?: ""
            val total = backStackEntry.arguments?.getString("total") ?: ""

            val viewModel: ClienteViewModel = viewModel()

            ValoracionesScreen(
                viewModel = viewModel,
                idReserva = idReserva,
                paquete = paquete,
                factura = factura,
                total = total,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = "sugerencias") {
            SugerenciasScreen(
                viewModel = viewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = "encuesta") {
            EncuestaScreen(
                viewModel = viewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- 3. GERENCIA ---
        composable("gerencia") {
            GerenciaScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPaquetesVendidos = { navController.navigate("paquetes_vendidos") },
                onNavigateToIngresosDestino = { navController.navigate("ingresos_destino") },
                onNavigateToTopClientes = { navController.navigate("top_clientes") }
            )
        }

        composable("paquetes_vendidos") {
            PaquetesVendidosScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable("ingresos_destino") {
            IngresosDestinoScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable("top_clientes") {
            TopClientesScreen(onNavigateBack = { navController.popBackStack() })
        }

        // --- 4. ASISTENTE ---
        composable(RutasAsistente.MenuAsistente.ruta) {
            MenuAsistenteScreen(
                onNavigateToClientes = { navController.navigate(RutasAsistente.ListaClientes.ruta) },
                onNavigateToServicios = { navController.navigate(RutasAsistente.ServiciosMenu.ruta) },
                onNavigateToReservas = { navController.navigate(RutasAsistente.PanelReservas.ruta) },
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // --- 📋 PANEL DE RESERVAS ---
        composable(RutasAsistente.PanelReservas.ruta) {
            ReservaPanelScreen(
                viewModel = reservaPanelViewModel,
                token = ClienteViewModel.authToken,
                onNavigateToAgregar = { navController.navigate(RutasAsistente.CrearReserva.ruta) },
                onNavigateToEditar = { idReserva ->
                    navController.navigate(RutasAsistente.EditarReserva.crearRuta(idReserva))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- ➕ CREAR RESERVA ---
        composable(RutasAsistente.CrearReserva.ruta) {
            val context = LocalContext.current
            val token = ClienteViewModel.authToken
            val clientes by reservaPanelViewModel.clientes.collectAsState()
            val paquetes by reservaPanelViewModel.paquetes.collectAsState()
            val estados by reservaPanelViewModel.estados.collectAsState()
            val tiposPago by reservaPanelViewModel.tiposPago.collectAsState()

            LaunchedEffect(Unit) { reservaPanelViewModel.cargarCatalogos(token) }

            CrearReservaScreen(
                clientes = clientes,
                paquetes = paquetes,
                estados = estados,
                tiposPago = tiposPago,
                onAgregar = { clienteId, paqueteId, estadoId, pagoId ->
                    reservaPanelViewModel.crearReserva(
                        token = token,
                        request = ReservaRequest(
                            clientId = clienteId,
                            packageId = paqueteId,
                            paymentTypeId = pagoId,
                            statusId = estadoId
                        )
                    ) { exito ->
                        if (exito) {
                            Toast.makeText(context, "✅ Reserva agregada con éxito", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            val detalle = reservaPanelViewModel.errorMessage.value ?: "Error desconocido"
                            Toast.makeText(context, "❌ $detalle", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                onCancelar = { navController.popBackStack() }
            )
        }

        // --- ✏️ EDITAR RESERVA ---
        composable(
            route = RutasAsistente.EditarReserva.ruta,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val context = LocalContext.current
            val token = ClienteViewModel.authToken
            val idReserva = backStackEntry.arguments?.getInt("id") ?: 0

            val clientes by reservaPanelViewModel.clientes.collectAsState()
            val paquetes by reservaPanelViewModel.paquetes.collectAsState()
            val estados by reservaPanelViewModel.estados.collectAsState()
            val tiposPago by reservaPanelViewModel.tiposPago.collectAsState()
            val reservas by reservaPanelViewModel.reservas.collectAsState()

            val reservaActual = reservas.find { it.id == idReserva }

            LaunchedEffect(Unit) {
                reservaPanelViewModel.cargarCatalogos(token)
                if (reservas.isEmpty()) {
                    reservaPanelViewModel.cargarReservas(token)
                }
            }

            if (reservaActual == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val estadoInicial = reservaActual.currentStatus?.id ?: 0

                EditarReservaScreen(
                    clientes = clientes,
                    paquetes = paquetes,
                    estados = estados,
                    tiposPago = tiposPago,
                    clienteInicialId = reservaActual.clienteId ?: 0,
                    paqueteInicialId = reservaActual.paqueteId ?: 0,
                    estadoInicialId = estadoInicial,
                    pagoInicialId = reservaActual.tipoPagoId ?: 0,
                    paqueteInicialNombre = reservaActual.paqueteNombre,
                    estadoBloqueado = !reservaActual.estado.equals("Pendiente", true),
                    onActualizar = { clienteId, paqueteId, estadoId, pagoId ->
                        // PUT: solo cliente, paquete, pago y vendedor (el estado va aparte)
                        val campos = hashMapOf<String, Any>(
                            "client" to clienteId,
                            "package_id" to paqueteId,
                            "payment_type" to pagoId,
                            "seller_user" to (reservaActual.sellerUserId ?: 1)
                        )
                        val estadoCambio = estadoId != estadoInicial

                        reservaPanelViewModel.editarReserva(
                            token = token,
                            id = idReserva,
                            campos = campos,
                            estadoNuevoId = if (estadoCambio) estadoId else null
                        ) { exito, mensaje ->
                            if (exito) {
                                Toast.makeText(context, "✅ Reserva actualizada con éxito", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "❌ ${mensaje ?: "Error desconocido"}", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onLimpiar = {},
                    onCancelar = { navController.popBackStack() },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(RutasAsistente.ServiciosMenu.ruta) {
            ServiciosMenuScreen(
                onNavigateToVuelos = { navController.navigate(RutasAsistente.ListaVuelos.ruta) },
                onNavigateToTransporte = { navController.navigate(RutasAsistente.ListaTransportes.ruta) },
                onNavigateToHoteles = { navController.navigate(RutasAsistente.ListaHoteles.ruta) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- ✈️ VUELOS ---
        composable(RutasAsistente.ListaVuelos.ruta) {
            VuelosListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAgregar = {
                    navController.navigate(RutasAsistente.FormularioVuelo.crearRuta("crear", 0))
                },
                onNavigateToEditar = { idVuelo ->
                    navController.navigate(RutasAsistente.FormularioVuelo.crearRuta("editar", idVuelo))
                }
            )
        }

        composable(
            route = RutasAsistente.FormularioVuelo.ruta,
            arguments = listOf(
                navArgument("modo") { type = NavType.StringType },
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val modo = backStackEntry.arguments?.getString("modo") ?: "crear"
            val id = backStackEntry.arguments?.getInt("id") ?: 0

            VueloFormScreen(modo = modo, vueloId = id, onNavigateBack = { navController.popBackStack() })
        }

        // --- 🚌 TRANSPORTES ---
        composable(RutasAsistente.ListaTransportes.ruta) {
            TransportesListScreen(
                viewModel = transporteViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAgregar = {
                    navController.navigate(RutasAsistente.FormularioTransporte.crearRuta("crear", 0))
                },
                onNavigateToEditar = { idTransporte ->
                    navController.navigate(RutasAsistente.FormularioTransporte.crearRuta("editar", idTransporte))
                }
            )
        }

        composable(
            route = RutasAsistente.FormularioTransporte.ruta,
            arguments = listOf(
                navArgument("modo") { type = NavType.StringType },
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val modo = backStackEntry.arguments?.getString("modo") ?: "crear"
            val id = backStackEntry.arguments?.getInt("id") ?: 0

            TransporteFormScreen(
                modo = modo,
                transporteId = id,
                viewModel = transporteViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- 🏨 HOTELES ---
        composable(RutasAsistente.ListaHoteles.ruta) {
            HotelesListScreen(
                viewModel = hotelViewModel,
                onNavigateToForm = { idHotel ->
                    if (idHotel == null) {
                        navController.navigate(RutasAsistente.FormularioHotel.crearRuta("crear", 0))
                    } else {
                        navController.navigate(RutasAsistente.FormularioHotel.crearRuta("editar", idHotel))
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = RutasAsistente.FormularioHotel.ruta,
            arguments = listOf(
                navArgument("modo") { type = NavType.StringType },
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val modo = backStackEntry.arguments?.getString("modo") ?: "crear"
            val id = backStackEntry.arguments?.getInt("id") ?: 0

            HotelFormScreen(
                viewModel = hotelViewModel,
                hotelId = if (modo == "crear") null else id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- 👤 CLIENTES ---
        composable(RutasAsistente.ListaClientes.ruta) {
            ClientesListScreen(
                onNavigateToNuevo = {
                    navController.navigate(RutasAsistente.FormularioCliente.crearRuta("crear", 0))
                },
                onNavigateToEditar = { idCliente ->
                    navController.navigate(RutasAsistente.FormularioCliente.crearRuta("editar", idCliente))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = RutasAsistente.FormularioCliente.ruta,
            arguments = listOf(
                navArgument("modo") { type = NavType.StringType },
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val modo = backStackEntry.arguments?.getString("modo") ?: "crear"
            val id = backStackEntry.arguments?.getInt("id") ?: 0

            ClienteFormScreen(modo = modo, clienteId = id, onNavigateBack = { navController.popBackStack() })
        }
    }
}