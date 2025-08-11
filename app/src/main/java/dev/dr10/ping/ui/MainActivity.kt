package dev.dr10.ping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.dr10.ping.ui.navigation.NavHost
import dev.dr10.ping.ui.theme.AppTheme
import dev.dr10.ping.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AppTheme.colors.background.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(AppTheme.colors.background.toArgb())
        )
        setContent {
            val scope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }

            val viewModel: MainViewModel = koinViewModel()
            val isLogged = viewModel.isUserLoggedIn()
            val isCompletedProfile = viewModel.isProfileSetupCompleted()

            splashScreen.setKeepOnScreenCondition { false }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState) {
                        Snackbar(
                            snackbarData = it,
                            containerColor = AppTheme.colors.background,
                            contentColor = Color(0xFFFA4C48),
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    isLogged = isLogged,
                    isCompletedProfile = isCompletedProfile,
                ) { message ->
                    scope.launch { snackBarHostState.showSnackbar(getString(message)) }
                }
            }
        }
    }
}
