package com.example.prestamolabctma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.prestamolabctma.model.Equipo
import com.example.prestamolabctma.model.SolicitudPrestamo
import com.example.prestamolabctma.model.Validaciones
import com.example.prestamolabctma.repository.InMemoryPrestamoRepository
import com.example.prestamolabctma.repository.PrestamoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PrestamoViewModel(
    private val repository: PrestamoRepository = InMemoryPrestamoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrestamoUiState())
    val uiState: StateFlow<PrestamoUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        _uiState.update {
            it.copy(
                equipos = repository.obtenerEquipos(),
                solicitudes = repository.obtenerSolicitudes()
            )
        }
    }

    fun seleccionarEquipo(equipoId: Int) {
        _uiState.update { it.copy(equipoSeleccionadoId = equipoId, mensajeError = null) }
    }

    fun onAmbienteChanged(nuevoAmbiente: String) {
        _uiState.update { it.copy(ambienteDestino = nuevoAmbiente, mensajeError = null) }
    }

    fun onPropositoChanged(nuevoProposito: String) {
        _uiState.update { it.copy(proposito = nuevoProposito, mensajeError = null) }
    }

    fun onDuracionChanged(nuevaDuracion: String) {
        _uiState.update { it.copy(duracionHoras = nuevaDuracion, mensajeError = null) }
    }

    fun limpiarMensajes() {
        _uiState.update { it.copy(mensajeError = null, mensajeExito = null) }
    }

    fun crearSolicitud(): Boolean {
        val state = _uiState.value

        if (state.guardando) return false

        val equipoId = state.equipoSeleccionadoId
        if (equipoId == null) {
            _uiState.update { it.copy(mensajeError = "Debe seleccionar un equipo.") }
            return false
        }

        if (!Validaciones.ambienteValido(state.ambienteDestino)) {
            _uiState.update { it.copy(mensajeError = "El ambiente destino no puede estar vacío.") }
            return false
        }

        if (!Validaciones.propositoValido(state.proposito)) {
            _uiState.update { it.copy(mensajeError = "El propósito debe tener entre 10 y 180 caracteres (RN-03).") }
            return false
        }

        val duracionInt = state.duracionHoras.toIntOrNull() ?: 0
        if (!Validaciones.duracionValida(duracionInt)) {
            _uiState.update { it.copy(mensajeError = "La duración debe ser entre 1 y 8 horas (RN-04).") }
            return false
        }

        _uiState.update { it.copy(guardando = true) }

        val resultado = repository.crearSolicitud(
            equipoId = equipoId,
            ambienteDestino = state.ambienteDestino,
            proposito = state.proposito,
            duracionHoras = duracionInt
        )

        return resultado.fold(
            onSuccess = {
                _uiState.update {
                    it.copy(
                        guardando = false,
                        ambienteDestino = "",
                        proposito = "",
                        duracionHoras = "1",
                        equipoSeleccionadoId = null,
                        mensajeExito = "Solicitud creada exitosamente.",
                        equipos = repository.obtenerEquipos(),
                        solicitudes = repository.obtenerSolicitudes()
                    )
                }
                true
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        guardando = false,
                        mensajeError = error.message ?: "Error al crear la solicitud."
                    )
                }
                false
            }
        )
    }

    fun cancelarSolicitud(solicitudId: Int) {
        val resultado = repository.cancelarSolicitud(solicitudId)
        resultado.fold(
            onSuccess = {
                _uiState.update {
                    it.copy(
                        mensajeExito = "Solicitud cancelada correctamente.",
                        equipos = repository.obtenerEquipos(),
                        solicitudes = repository.obtenerSolicitudes()
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        mensajeError = error.message ?: "Error al cancelar la solicitud."
                    )
                }
            }
        )
    }
}