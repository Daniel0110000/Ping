package com.daniel.ping.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.daniel.ping.ui.screens.ChatScreen
import com.daniel.ping.ui.screens.HomeScreen
import com.daniel.ping.ui.screens.RedScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController = navController, startDestination = ScreenRoutes.Home.route, modifier = modifier){
        composable(route = ScreenRoutes.Home.route){
            HomeScreen(navController)
        }
        composable(route = ScreenRoutes.Red.route){
            RedScreen(navController)
        }
        composable(route = ScreenRoutes.Chat.route){
            ChatScreen(navController)
        }
    }
}