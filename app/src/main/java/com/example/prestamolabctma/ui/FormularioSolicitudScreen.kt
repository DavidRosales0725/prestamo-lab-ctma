package com.example.prestamolabctma.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prestamolabctma.viewmodel.PrestamoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioSolicitudScreen(
    state: PrestamoUiState,
    onAmbienteChanged: (String) -> Unit,
    onPropositoChanged: (String) -> Unit,
    onDuracionChanged: (String) -> Unit,
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Formulario de Solicitud") }) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Equipo ID Seleccionado: ${state.equipoSeleccionadoId ?: "Ninguno"}",
                style = MaterialTheme.typography.titleSmall
            )

            OutlinedTextField(
                value = state.ambienteDestino,
                onValueChange = onAmbienteChanged,
                label = { Text("Ambiente Destino (Ej: Aula 302)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.proposito,
                onValueChange = onPropositoChanged,
                label = { Text("Propósito (10 - 180 caracteres)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = state.duracionHoras,
                onValueChange = onDuracionChanged,
                label = { Text("Duración en Horas (1 - 8)") },
                modifier = Modifier.fillMaxWidth()
            )

            state.mensajeError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            state.mensajeExito?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = onGuardarClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.guardando && state.equipoSeleccionadoId != null
            ) {
                Text(if (state.guardando) "Guardando..." else "Enviar Solicitud")
            }
        }
    }
}