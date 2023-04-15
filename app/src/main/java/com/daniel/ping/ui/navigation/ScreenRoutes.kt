package com.daniel.ping.ui.navigation

sealed class ScreenRoutes(val route: String){
    object Home: ScreenRoutes(route = "home_screen")
    object Red: ScreenRoutes(route = "red_screen")
    object Chat: ScreenRoutes(route = "chat_screen")
}