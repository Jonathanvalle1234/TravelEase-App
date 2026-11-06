package com.example.travelease.view

import androidx.compose.runtime.Composable
import com.example.travelease.viewmodel.ReportViewModel

@Composable
fun TopClientesScreen(viewModel: ReportViewModel, onNavigateBack: () -> Unit) {
    PowerBiReportScreen("Top Clientes", "top-clientes", viewModel, onNavigateBack)
}
