package dev.dr10.ping.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavDestination: NavKey {
    @Serializable
    data object SignIn: NavDestination
    @Serializable
    data object SignUp: NavDestination
    @Serializable
    data object SetupProfile: NavDestination
    @Serializable
    data object InitialSync: NavDestination
    @Serializable
    data object Home: NavDestination

}