package com.example.prestamolabctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prestamolabctma.ui.CatalogoScreen
import com.example.prestamolabctma.ui.FormularioSolicitudScreen
import com.example.prestamolabctma.ui.MisSolicitudesScreen
import com.example.prestamolabctma.viewmodel.PrestamoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PrestamoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val uiState by viewModel.uiState.collectAsState()
                val navController = rememberNavController()
                val currentEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry?.destination?.route ?: "catalogo"

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentRoute == "catalogo",
                                onClick = { navController.navigate("catalogo") },
                                label = { Text("Catálogo") },
                                icon = { Text("📦") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == "formulario",
                                onClick = { navController.navigate("formulario") },
                                label = { Text("Solicitar") },
                                icon = { Text("📝") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == "solicitudes",
                                onClick = { navController.navigate("solicitudes") },
                                label = { Text("Historial") },
                                icon = { Text("📋") }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "catalogo",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("catalogo") {
                            CatalogoScreen(
                                state = uiState,
                                onEquipoSeleccionado = { equipoId ->
                                    viewModel.seleccionarEquipo(equipoId)
                                    navController.navigate("formulario")
                                }
                            )
                        }
                        composable("formulario") {
                            FormularioSolicitudScreen(
                                state = uiState,
                                onAmbienteChanged = viewModel::onAmbienteChanged,
                                onPropositoChanged = viewModel::onPropositoChanged,
                                onDuracionChanged = viewModel::onDuracionChanged,
                                onGuardarClick = {
                                    if (viewModel.crearSolicitud()) {
                                        navController.navigate("solicitudes")
                                    }
                                }
                            )
                        }
                        composable("solicitudes") {
                            MisSolicitudesScreen(
                                state = uiState,
                                onCancelarSolicitud = viewModel::cancelarSolicitud
                            )
                        }
                    }
                }
            }
        }
    }
}