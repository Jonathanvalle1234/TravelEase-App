package com.example.travelease.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelease.model.PowerBiEmbedResponse
import com.example.travelease.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ReportUiState {
    data object Loading : ReportUiState
    data class Success(val report: PowerBiEmbedResponse) : ReportUiState
    data class Error(val message: String) : ReportUiState
}

class ReportViewModel : ViewModel() {
    private val repository = ReportRepository()
    private val _state = MutableStateFlow<ReportUiState>(ReportUiState.Loading)
    val state: StateFlow<ReportUiState> = _state

    fun cargar(key: String) {
        viewModelScope.launch {
            _state.value = ReportUiState.Loading
            repository.obtenerReporte(key).fold(
                onSuccess = { _state.value = ReportUiState.Success(it) },
                onFailure = { _state.value = ReportUiState.Error(it.message ?: "No se pudo cargar el reporte.") }
            )
        }
    }
}
