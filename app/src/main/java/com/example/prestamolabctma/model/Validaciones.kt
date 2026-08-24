package com.example.prestamolabctma.model

object Validaciones {
    fun ambienteValido(ambiente: String): Boolean {
        return ambiente.isNotBlank()
    }

    fun propositoValido(proposito: String): Boolean {
        val longitud = proposito.trim().length
        return longitud in 10..180
    }

    fun duracionValida(duracion: Int): Boolean {
        return duracion in 1..8
    }
}