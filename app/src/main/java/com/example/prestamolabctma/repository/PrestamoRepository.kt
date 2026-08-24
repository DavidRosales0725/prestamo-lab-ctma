package com.example.prestamolabctma.repository

import com.example.prestamolabctma.model.Equipo
import com.example.prestamolabctma.model.SolicitudPrestamo

/**
 * Contrato de acceso a datos del dominio PréstamoLab.
 * La UI y el ViewModel solo conocen esta interfaz, nunca la
 * implementación concreta — así se puede reemplazar el InMemory
 * por otra fuente en el futuro sin tocar el resto de capas.
 */
interface PrestamoRepository {
    fun obtenerEquipos(): List<Equipo>
    fun obtenerEquipo(id: Int): Equipo?
    fun obtenerSolicitudes(): List<SolicitudPrestamo>
    fun obtenerSolicitud(id: Int): SolicitudPrestamo?
    fun crearSolicitud(
        equipoId: Int,
        ambienteDestino: String,
        proposito: String,
        duracionHoras: Int
    ): Result<SolicitudPrestamo>
    fun cancelarSolicitud(id: Int): Result<Unit>
}