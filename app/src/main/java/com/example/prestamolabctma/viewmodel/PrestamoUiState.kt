package com.example.prestamolabctma.viewmodel

import com.example.prestamolabctma.model.Equipo
import com.example.prestamolabctma.model.SolicitudPrestamo

data class PrestamoUiState(
    val equipos: List<Equipo> = emptyList(),
    val solicitudes: List<SolicitudPrestamo> = emptyList(),
    val equipoSeleccionadoId: Int? = null,
    val ambienteDestino: String = "",
    val proposito: String = "",
    val duracionHoras: String = "1",
    val mensajeError: String? = null,
    val mensajeExito: String? = null,
    val guardando: Boolean = false
)