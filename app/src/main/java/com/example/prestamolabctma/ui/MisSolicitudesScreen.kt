package com.example.prestamolabctma.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prestamolabctma.model.EstadoSolicitud
import com.example.prestamolabctma.viewmodel.PrestamoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisSolicitudesScreen(
    state: PrestamoUiState,
    onCancelarSolicitud: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Solicitudes Realizadas") }) }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.solicitudes) { solicitud ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Solicitud #${solicitud.id}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Equipo ID: ${solicitud.equipoId}")
                        Text(text = "Ambiente: ${solicitud.ambienteDestino}")
                        Text(text = "Propósito: ${solicitud.proposito}")
                        Text(text = "Duración: ${solicitud.duracionHoras} hrs")
                        Text(text = "Estado: ${solicitud.estado}")

                        Spacer(modifier = Modifier.height(8.dp))

                        if (solicitud.estado == EstadoSolicitud.SOLICITADA) {
                            OutlinedButton(
                                onClick = { onCancelarSolicitud(solicitud.id) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Cancelar Solicitud")
                            }
                        }
                    }
                }
            }
        }
    }
}