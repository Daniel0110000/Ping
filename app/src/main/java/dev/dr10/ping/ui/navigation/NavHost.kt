package dev.dr10.ping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.dr10.ping.ui.screens.HomeContainerScreen
import dev.dr10.ping.ui.screens.InitialSyncScreen
import dev.dr10.ping.ui.screens.auth.ProfileSetupScreen
import dev.dr10.ping.ui.screens.auth.SignInScreen
import dev.dr10.ping.ui.screens.auth.SignUpScreen

@Composable
fun NavHost(
    modifier: Modifier,
    isLogged: Boolean,
    isCompletedProfile: Boolean,
    isInitialSyncCompleted: Boolean,
    onErrorMessage: (Int) -> Unit
) {
    val backStack = rememberNavBackStack(
        when {
            isLogged && isCompletedProfile -> NavDestination.Home
            isLogged -> NavDestination.SetupProfile
            isInitialSyncCompleted -> NavDestination.InitialSync
            else -> NavDestination.SignIn
        }
    )
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<NavDestination.SignIn> {
                SignInScreen(
                    onErrorMessage = { onErrorMessage(it) },
                    onNavigateToSignUp = { backStack.add(NavDestination.SignUp) },
                    onNavigateToLoadingMessages = {
                        backStack.clear()
                        backStack.add(NavDestination.InitialSync)
                    },
                    onNavigateToProfileSetup = {
                        backStack.clear()
                        backStack.add(NavDestination.SetupProfile)
                    }
                )
            }
            entry<NavDestination.SignUp> {
                SignUpScreen(
                    onErrorMessage = { onErrorMessage(it) },
                    onNavigateToSignIn = { backStack.removeLastOrNull() },
                    onNavigateToSetupProfile = {
                        backStack.clear()
                        backStack.add(NavDestination.SetupProfile)
                    },
                    onNavigateToLoadingMessages = {
                        backStack.clear()
                        backStack.add(NavDestination.InitialSync)
                    }
                )
            }
            entry<NavDestination.SetupProfile> { ProfileSetupScreen(
                onErrorMessage = { onErrorMessage(it) },
                onNavigateToHome = {
                    backStack.clear()
                    backStack.add(NavDestination.Home)
                }
            ) }
            entry<NavDestination.InitialSync> { InitialSyncScreen(
                    onErrorMessage = { onErrorMessage(it) },
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(NavDestination.Home)
                    }
                )
            }
            entry<NavDestination.Home> { HomeContainerScreen { onErrorMessage(it) } }
        }
    )
}