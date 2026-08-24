package com.example.prestamolabctma.repository

import com.example.prestamolabctma.model.CategoriaEquipo
import com.example.prestamolabctma.model.Equipo
import com.example.prestamolabctma.model.EstadoEquipo
import com.example.prestamolabctma.model.EstadoSolicitud
import com.example.prestamolabctma.model.SolicitudPrestamo

/**
 * Fuente de verdad única en memoria, compartida durante la ejecución
 * de la app (RN-09: datos sintéticos, sin persistencia real).
 *
 * Es la única clase que puede modificar las listas internas —
 * la UI y el ViewModel solo ven la interfaz PrestamoRepository.
 */
class InMemoryPrestamoRepository : PrestamoRepository {

    // --- Datos semilla sintéticos ---
    private var equipos = mutableListOf(
        Equipo(1, "Multímetro digital", CategoriaEquipo.EQUIPO_MEDICION, EstadoEquipo.DISPONIBLE),
        Equipo(2, "Taladro inalámbrico", CategoriaEquipo.HERRAMIENTA_ELECTRICA, EstadoEquipo.DISPONIBLE),
        Equipo(3, "Kit de destornilladores", CategoriaEquipo.HERRAMIENTA_MANUAL, EstadoEquipo.DISPONIBLE),
        Equipo(4, "Tableta Android", CategoriaEquipo.EQUIPO_COMPUTO, EstadoEquipo.DISPONIBLE),
        Equipo(5, "Osciloscopio", CategoriaEquipo.EQUIPO_LABORATORIO, EstadoEquipo.RESERVADO)
    )

    private var solicitudes = mutableListOf<SolicitudPrestamo>()

    private var siguienteSolicitudId = 1

    // --- Consultas ---

    override fun obtenerEquipos(): List<Equipo> = equipos.toList()

    override fun obtenerEquipo(id: Int): Equipo? =
        equipos.find { it.id == id }

    override fun obtenerSolicitudes(): List<SolicitudPrestamo> = solicitudes.toList()

    override fun obtenerSolicitud(id: Int): SolicitudPrestamo? =
        solicitudes.find { it.id == id }

    // --- Comandos ---

    /**
     * Crea una solicitud solo si el equipo existe y está DISPONIBLE (RN-01).
     * Al crearla, el equipo pasa a RESERVADO (RN-06).
     * Esta función es sincronizada a nivel lógico dentro de una sola
     * llamada: no hay forma de que dos solicitudes se cuelen entre
     * la validación y la escritura porque todo ocurre en el mismo hilo
     * de la función — el ViewModel es responsable de no invocarla dos
     * veces por doble clic (RN-05, lo resolveremos ahí con un flag
     * "guardando").
     */
    override fun crearSolicitud(
        equipoId: Int,
        ambienteDestino: String,
        proposito: String,
        duracionHoras: Int
    ): Result<SolicitudPrestamo> {
        val equipo = equipos.find { it.id == equipoId }
            ?: return Result.failure(IllegalArgumentException("El equipo no existe."))

        if (equipo.estado != EstadoEquipo.DISPONIBLE) {
            return Result.failure(IllegalStateException("El equipo no está disponible."))
        }

        val nuevaSolicitud = SolicitudPrestamo(
            id = siguienteSolicitudId++,
            equipoId = equipoId,
            ambienteDestino = ambienteDestino,
            proposito = proposito,
            duracionHoras = duracionHoras,
            estado = EstadoSolicitud.SOLICITADA
        )
        solicitudes.add(nuevaSolicitud)

        // Reserva el equipo (RN-06)
        val index = equipos.indexOfFirst { it.id == equipoId }
        equipos[index] = equipo.copy(estado = EstadoEquipo.RESERVADO)

        return Result.success(nuevaSolicitud)
    }

    /**
     * Solo se puede cancelar una solicitud en estado SOLICITADA (RN-07).
     * Al cancelar, el equipo vuelve a estar DISPONIBLE.
     */
    override fun cancelarSolicitud(id: Int): Result<Unit> {
        val solicitud = solicitudes.find { it.id == id }
            ?: return Result.failure(IllegalArgumentException("La solicitud no existe."))

        if (solicitud.estado != EstadoSolicitud.SOLICITADA) {
            return Result.failure(IllegalStateException("Solo se puede cancelar una solicitud en estado SOLICITADA."))
        }

        val index = solicitudes.indexOfFirst { it.id == id }
        solicitudes[index] = solicitud.copy(estado = EstadoSolicitud.CANCELADA)

        val equipoIndex = equipos.indexOfFirst { it.id == solicitud.equipoId }
        if (equipoIndex != -1) {
            equipos[equipoIndex] = equipos[equipoIndex].copy(estado = EstadoEquipo.DISPONIBLE)
        }

        return Result.success(Unit)
    }
}