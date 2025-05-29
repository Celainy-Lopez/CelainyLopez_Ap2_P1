package edu.ucne.celainylopez_ap2_p1.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object SistemaList: Screen()

    @Serializable
    data class Sistema(val SistemaId: Int?) : Screen()
}