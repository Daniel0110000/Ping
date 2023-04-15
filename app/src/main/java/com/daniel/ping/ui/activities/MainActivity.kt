package com.daniel.ping.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daniel.ping.ui.screens.MainScreen
import com.daniel.ping.ui.theme.PingTheme

class MainActivity : ComponentActivity() {

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
