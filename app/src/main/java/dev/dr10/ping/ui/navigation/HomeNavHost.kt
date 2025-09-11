package dev.dr10.ping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import dev.dr10.ping.domain.mappers.toProfileModel
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.ui.screens.NetworkScreen
import dev.dr10.ping.ui.screens.chats.ChatScreen
import dev.dr10.ping.ui.screens.components.HomePlaceholder
import kotlin.reflect.typeOf

@Composable
fun HomeNavHost(
    navHostController: NavHostController,
    modifier: Modifier,
    onBack: () -> Unit,
    onNavigateToChat: (UserProfileModel) -> Unit,
    onErrorMessage: (Int) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = HomePlaceHolder,
        modifier = modifier
    ) {
        composable<HomePlaceHolder> { HomePlaceholder() }
        composable<Network> {
            NetworkScreen(
                onBack = { onBack() },
                onNavigateToChat = { onNavigateToChat(it) },
                onErrorMessage = { onErrorMessage(it) }
            )
        }
        composable<Chat>(
            typeMap = mapOf(typeOf<String>() to NavType.StringType),
            deepLinks = listOf(
                navDeepLink { uriPattern = "${Constants.NOTIFICATION_DEEP_LINK_BASE_URL}{userData}" }
            )
        ) { backStack ->
            ChatScreen(
                receiverData = backStack.toRoute<Chat>().userData.toProfileModel(),
                onBack = { onBack() },
                onErrorMessage = { onErrorMessage(it) }
            )
        }
    }
}