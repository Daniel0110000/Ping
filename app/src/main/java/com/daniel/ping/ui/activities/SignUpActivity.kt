package com.daniel.ping.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.ping.ui.screens.SignUpScreen
import com.daniel.ping.ui.theme.PingTheme
import com.daniel.ping.ui.viewModels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {

    // ViewModel instance is created using viewModels delegate
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PingTheme {
                // Collect the state of the ViewModel using StateFlow
                val state by viewModel.state.collectAsState()
                // Call SignUpScreen composable function with state, context, activity, and onRedirect callback arguments
                SignUpScreen(
                    state = state,
                    context = this,
                    activity = this,
                    onRedirect = { onBack() },
                    onRedirectToProfileSetup = {
                        // On button click, navigate to the ProfileSetupActivity
                        redirectActivity(ProfileSetupActivity::class.java)
                    },
                    onRedirectToMainScreen = {
                        // Navigate to the MainActivity
                        redirectActivity(MainActivity::class.java)
                    }
                )
                // Handle back button press with BackHandler composable function
                BackHandler { onBack() }
            }
        }
    }

    private fun redirectActivity(activity: Class<*>){
        val intent = Intent(this, activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Animatoo.animateSlideLeft(this)
    }

    // Function to handle back button press
    private fun onBack() {
        finish()
        Animatoo.animateSlideRight(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleActivityResult(requestCode, resultCode, data)
    }

}