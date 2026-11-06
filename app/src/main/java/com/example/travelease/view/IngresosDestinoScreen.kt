package com.example.travelease.view

import androidx.compose.runtime.Composable
import com.example.travelease.viewmodel.ReportViewModel

@Composable
fun IngresosDestinoScreen(viewModel: ReportViewModel, onNavigateBack: () -> Unit) {
    PowerBiReportScreen("Ingresos por Destino", "ingreso-por-destino-anual", viewModel, onNavigateBack)
}
