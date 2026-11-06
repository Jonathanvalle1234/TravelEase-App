package com.example.travelease.view

import androidx.compose.runtime.Composable
import com.example.travelease.viewmodel.ReportViewModel

@Composable
fun EvolucionVentasScreen(viewModel: ReportViewModel, onNavigateBack: () -> Unit) {
    PowerBiReportScreen("Evolución de ventas por año", "evolucion-ventas-anual", viewModel, onNavigateBack)
}
