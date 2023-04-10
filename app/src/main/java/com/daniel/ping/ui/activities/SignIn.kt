package com.daniel.ping.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.daniel.ping.ui.theme.PingTheme

class SignIn : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContent {
            PingTheme {
                splashScreen.setKeepOnScreenCondition{ false }
            }
        }
    }
}