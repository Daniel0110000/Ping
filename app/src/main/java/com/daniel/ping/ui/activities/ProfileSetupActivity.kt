package com.daniel.ping.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.ping.ui.screens.ProfileSetupScreen
import com.daniel.ping.ui.theme.PingTheme
import com.daniel.ping.ui.viewModels.ProfileSetupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetupActivity : ComponentActivity() {

    // This activity has a view model instance of ProfileSetupViewModel
    private val viewModel: ProfileSetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The state of the view model is collected as a state object
        // The state object is passed to the ProfileSetupScreen composable function
        setContent {
            PingTheme {
                val state by viewModel.state.collectAsState()

                // The ProfileSetupScreen composable function takes the state object as input and a lambda expression to handle button clicks
                // When the button is clicked, the MainActivity is launched and any other activities in the back stack are cleared
                ProfileSetupScreen(state = state) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this)
                }
            }
        }
    }
}
