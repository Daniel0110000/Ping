package com.daniel.ping.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.ping.ui.screens.SignUpScreen
import com.daniel.ping.ui.theme.PingTheme

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PingTheme {
                SignUpScreen { onBack() }
                BackHandler { onBack() }
            }
        }
    }

    private fun onBack() {
        finish()
        Animatoo.animateSlideRight(this)
    }

}