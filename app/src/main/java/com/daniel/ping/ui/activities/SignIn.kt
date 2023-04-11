package com.daniel.ping.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.ping.ui.screens.SignInScreen
import com.daniel.ping.ui.theme.PingTheme

class SignIn : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContent {
            PingTheme {
                splashScreen.setKeepOnScreenCondition{ false }
                SignInScreen{
                    startActivity(Intent(this, SignUp::class.java))
                    Animatoo.animateSlideLeft(this)
                }
            }
        }
    }

}