package dev.dr10.ping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.dr10.ping.ui.screens.NetworkScreen
import dev.dr10.ping.ui.screens.chats.ChatScreen
import dev.dr10.ping.ui.screens.components.HomePlaceholder

@Composable
fun HomeNavHost(
    backStack: NavBackStack,
    modifier: Modifier,
    onBack: () -> Unit,
    onErrorMessage: (Int) -> Unit
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<HomeNavDestination.HomePlaceHolder> { HomePlaceholder() }
            entry<HomeNavDestination.Network> {
                NetworkScreen(
                    onBack = { onBack() },
                    onErrorMessage = { onErrorMessage(it) }
                )
            }
            entry<HomeNavDestination.Chat> {
                ChatScreen()
            }
        }
    )

}