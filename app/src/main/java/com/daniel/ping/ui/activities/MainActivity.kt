package com.daniel.ping.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daniel.ping.ui.screens.MainScreen
import com.daniel.ping.ui.theme.PingTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection for this activity
 * MainActivity is the entry point for the app and sets up the navigation controller for the app
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PingTheme {
                navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}
