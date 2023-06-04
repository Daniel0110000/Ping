package com.daniel.ping.ui.navigation

/**
 * Sealed class that defines a set of screen routes as object instance
 * @param route The route associated with this screen, represented as a String
 */
sealed class ScreenRoutes(val route: String){
    // Object instance representing the Home screen
    object Home: ScreenRoutes(route = "home_screen")
    // Object instance representing the Network Users screen
    object NetworkUsers: ScreenRoutes(route = "network_users_screen")
    // Object instance representing the Chat Screen
    object Chat: ScreenRoutes(route = "chat_screen")

    // Object instance representing the Settings Screen
    object Settings: ScreenRoutes(route = "settings_screen")

}