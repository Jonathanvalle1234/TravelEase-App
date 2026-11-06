package com.example.travelease.view

import androidx.compose.runtime.Composable
import com.example.travelease.viewmodel.ReportViewModel

@Composable
fun PromedioEvaluacionesScreen(viewModel: ReportViewModel, onNavigateBack: () -> Unit) {
    PowerBiReportScreen("Promedio de evaluaciones", "promedio-encuesta-satisfaccion", viewModel, onNavigateBack)
}
