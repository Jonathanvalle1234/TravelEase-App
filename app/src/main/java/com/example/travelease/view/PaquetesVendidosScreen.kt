package com.example.travelease.view

import androidx.compose.runtime.Composable
import com.example.travelease.viewmodel.ReportViewModel

@Composable
fun PaquetesVendidosScreen(viewModel: ReportViewModel, onNavigateBack: () -> Unit) {
    PowerBiReportScreen("Paquetes más vendidos", "top-paquetes-turisticos", viewModel, onNavigateBack)
}
