package dev.dr10.ping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.dr10.ping.ui.screens.auth.SignInScreen
import dev.dr10.ping.ui.screens.auth.SignUpScreen

@Composable
fun NavHost(modifier: Modifier) {
    val backStack = rememberNavBackStack(NavDestination.SignIn)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<NavDestination.SignIn> { SignInScreen { backStack.add(NavDestination.SignUp) } }
            entry<NavDestination.SignUp> { SignUpScreen { backStack.removeLastOrNull() } }
        }
    )
}