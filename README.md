# PréstamoLab CTMA 🚀

Aplicación móvil Android para la gestión y control de solicitudes de préstamo de equipos en los laboratorios del CTMA.

## 🛠️ Tecnologías y Arquitectura
* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose + Material 3
* **Arquitectura:** MVVM (Model-View-ViewModel) + Repository Pattern
* **Gestión de Estado:** `StateFlow` / UI State reactivo
* **Navegación:** Compose Navigation (`NavHost`)

## 📌 Reglas de Negocio Implementadas (RN)
* **RN-01:** Filtrado y solicitud exclusiva de equipos en estado `DISPONIBLE`.
* **RN-03:** Validación en tiempo real del propósito del préstamo (mínimo 10, máximo 180 caracteres).
* **RN-04:** Validación de la duración de la solicitud (rango permitido de 1 a 8 horas).
* **RN-06:** Actualización automática del estado del equipo a `RESERVADO` al enviar la solicitud.
* **RN-07:** Cancelación de préstamos activos (estado `SOLICITADA`), restaurando el equipo a `DISPONIBLE`.

## 📂 Estructura del Proyecto
* `model/`: Definición de data classes y enumeradores (`Equipo`, `Solicitud`, estados).
* `repository/`: Gestión de datos en memoria y lógica de almacenamiento.
* `viewmodel/`: Manejo del estado reactivo `PrestamoUiState` y validaciones.
* `ui/`: Pantallas principales (`CatalogoScreen`, `FormularioSolicitudScreen`, `MisSolicitudesScreen`).
