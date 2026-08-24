package com.example.prestamolabctma.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prestamolabctma.model.EstadoEquipo
import com.example.prestamolabctma.viewmodel.PrestamoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    state: PrestamoUiState,
    onEquipoSeleccionado: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Catálogo de Equipos CTMA") }) }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.equipos) { equipo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = equipo.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Categoría: ${equipo.categoria}")
                            Text(text = "Estado: ${equipo.estado}")
                        }
                        Button(
                            onClick = { onEquipoSeleccionado(equipo.id) },
                            enabled = equipo.estado == EstadoEquipo.DISPONIBLE
                        ) {
                            Text("Solicitar")
                        }
                    }
                }
            }
        }
    }
}