package com.daniel.ping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.daniel.ping.ui.screens.ChatScreen
import com.daniel.ping.ui.screens.HomeScreen
import com.daniel.ping.ui.screens.NetworkUsersScreen

/**
 * A composable function that sets up the navigation graph and defines the routes for the app
 * @param navController the NavHostController that manages app navigation
 * @param modifier a Modifier to be applied to the NavHost
 */
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController = navController, startDestination = ScreenRoutes.Home.route, modifier = modifier){
        composable(route = ScreenRoutes.Home.route) {
            HomeScreen()
        }
        composable(route = ScreenRoutes.NetworkUsers.route) {
            NetworkUsersScreen(navController)
        }
        composable(route = ScreenRoutes.Chat.route) { entry ->
            ChatScreen(navController, entry.requiredArg("userDetails"))
        }
    }
}