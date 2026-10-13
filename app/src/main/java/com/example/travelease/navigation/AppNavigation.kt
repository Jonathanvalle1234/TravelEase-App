package com.example.travelease.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

// Importaciones de tus pantallas
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

// Importamos pantallas de vuelos
import com.example.travelease.view.VuelosListScreen
import com.example.travelease.view.VueloFormScreen

// 🚀 NUEVO: IMPORTAMOS PANTALLAS DE TRANSPORTE
import com.example.travelease.view.TransportesListScreen
import com.example.travelease.view.TransporteFormScreen

// 1. CLASE SEALED MEJORADA: Ahora acepta el ID en la ruta
sealed class RutasAsistente(val ruta: String) {
    object MenuAsistente : RutasAsistente("menu_asistente")
    object ListaClientes : RutasAsistente("lista_clientes")
    object ServiciosMenu : RutasAsistente("servicios_menu")

    object FormularioCliente : RutasAsistente("formulario_cliente/{modo}/{id}") {
        fun crearRuta(modo: String, id: Int = 0) = "formulario_cliente/$modo/$id"
    }

    object ListaVuelos : RutasAsistente("lista_vuelos")

    object FormularioVuelo : RutasAsistente("formulario_vuelo/{modo}/{id}") {
        fun crearRuta(modo: String, id: Int = 0) = "formulario_vuelo/$modo/$id"
    }

    // 🚀 2. NUEVAS RUTAS PARA TRANSPORTES
    object ListaTransportes : RutasAsistente("lista_transportes")

    object FormularioTransporte : RutasAsistente("formulario_transporte/{modo}/{id}") {
        fun crearRuta(modo: String, id: Int = 0) = "formulario_transporte/$modo/$id"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 🚀 NUEVO: Creamos un ViewModel compartido para todo el módulo de transportes
    val transporteViewModel: com.example.travelease.viewmodel.TransporteViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        // --- 1. RUTA DE INGRESO ---
        composable("login") {
            LoginScreen(
                onNavigateToCliente = { navController.navigate("cliente") },
                onNavigateToGerencia = { navController.navigate("gerencia") },
                onNavigateToAsistente = { navController.navigate(RutasAsistente.MenuAsistente.ruta) }
            )
        }

        // --- 2. RUTAS DEL CLIENTE ---
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

            val viewModel: ClienteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

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

        // --- 3. RUTAS DE GERENCIA ---
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

        // --- 4. RUTAS DEL ASISTENTE ---

        composable(RutasAsistente.MenuAsistente.ruta) {
            MenuAsistenteScreen(
                onNavigateToClientes = { navController.navigate(RutasAsistente.ListaClientes.ruta) },
                onNavigateToServicios = { navController.navigate(RutasAsistente.ServiciosMenu.ruta) },
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(RutasAsistente.ServiciosMenu.ruta) {
            ServiciosMenuScreen(
                onNavigateToVuelos = {
                    navController.navigate(RutasAsistente.ListaVuelos.ruta)
                },
                onNavigateToTransporte = {
                    navController.navigate(RutasAsistente.ListaTransportes.ruta)
                },
                onNavigateToHoteles = {
                    /* TODO: navController.navigate("lista_hoteles") */
                    println("Navegando a Hoteles")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- ✈️ MÓDULO DE VUELOS ---
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

        // --- 🚌 MÓDULO DE TRANSPORTES ---
        composable(RutasAsistente.ListaTransportes.ruta) {
            TransportesListScreen(
                viewModel = transporteViewModel, // 🚀 Le pasamos el compartido
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
                viewModel = transporteViewModel, // 🚀 Le pasamos el mismo compartido
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- 👤 MÓDULO DE CLIENTES ---
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