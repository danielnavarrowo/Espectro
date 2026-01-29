package com.dnavarro.espectro.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {
    @Serializable
    object Main : Screen()

    @Serializable
    object Screen2025 : Screen()

    @Serializable
    object Screen2026 : Screen()
}
