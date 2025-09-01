package dev.dr10.ping.ui.navigation

import androidx.navigation3.runtime.NavKey
import dev.dr10.ping.domain.models.UserProfileModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavDestination: NavKey {
    @Serializable
    data object HomePlaceHolder: HomeNavDestination

    @Serializable
    data object Network: HomeNavDestination

    @Serializable
    data class Chat(val userData: UserProfileModel): HomeNavDestination
}