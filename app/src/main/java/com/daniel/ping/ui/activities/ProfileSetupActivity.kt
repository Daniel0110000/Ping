package com.daniel.ping.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.daniel.ping.ui.screens.ProfileSetupScreen
import com.daniel.ping.ui.theme.PingTheme

class ProfileSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PingTheme {
                ProfileSetupScreen()
            }
        }
    }
}
