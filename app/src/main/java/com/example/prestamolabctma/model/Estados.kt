package com.example.prestamolabctma.model

enum class CategoriaEquipo {
    HERRAMIENTA_MANUAL,
    HERRAMIENTA_ELECTRICA,
    EQUIPO_MEDICION,
    EQUIPO_COMPUTO,
    EQUIPO_LABORATORIO
}

enum class EstadoEquipo {
    DISPONIBLE,
    RESERVADO,
    PRESTADO
}

enum class EstadoSolicitud {
    SOLICITADA,
    APROBADA,
    ENTREGADA,
    DEVUELTA,
    CANCELADA,
    RECHAZADA
}