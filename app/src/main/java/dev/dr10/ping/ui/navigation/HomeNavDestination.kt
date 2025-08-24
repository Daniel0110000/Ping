package dev.dr10.ping.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavDestination: NavKey {
    @Serializable
    data object HomePlaceHolder: HomeNavDestination

    @Serializable
    data object Network: HomeNavDestination

    @Serializable
    data object Chat: HomeNavDestination
}