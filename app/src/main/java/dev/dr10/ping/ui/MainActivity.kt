package dev.dr10.ping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.dr10.ping.ui.navigation.NavHost
import dev.dr10.ping.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AppTheme.colors.background.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(AppTheme.colors.background.toArgb())
        )
        setContent {
            splashScreen.setKeepOnScreenCondition { false }
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> NavHost(Modifier.padding(innerPadding)) }
        }
    }
}
