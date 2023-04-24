package com.daniel.ping.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.ping.ui.screens.SignInScreen
import com.daniel.ping.ui.theme.PingTheme
import com.daniel.ping.ui.viewModels.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {

    // ViewModel instance is created using viewModels delegate
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install a splash screen while the activity loads
        val splashScreen = installSplashScreen()

        // Check if the user's profile is complete and they are signed in, then start MainActivity and finish this activity
        if(viewModel.isCompletedProfile() && viewModel.isSignedIn()) startAndFinish(MainActivity::class.java)

        // Check if the user is signed in but their profile is not completed, then start ProfileSetupActivity and finish this activity
        else if(viewModel.isSignedIn()) startAndFinish(ProfileSetupActivity::class.java)
        setContent {
            PingTheme {
                // Hide the splash screen once the activity content is ready
                splashScreen.setKeepOnScreenCondition { false }

                // Collect the state of the ViewModel using StateFlow
                val state by viewModel.state.collectAsState()

                // Show the sign-in screen
                SignInScreen(
                    state = state,
                    context = this,
                    activity = this,
                    onRedirect = {
                        // On redirection, start the SignUpActivity and animate the screen transition
                        startActivity(Intent(this, SignUpActivity::class.java))
                        Animatoo.animateSlideLeft(this)
                    },
                    onRedirectToProfileSetup = {
                        // On button click, navigate to the ProfileSetupActivity
                        redirectActivity(ProfileSetupActivity::class.java)
                    },
                    onRedirectToMainScreen = {
                        // Navigate to the MainActivity
                        redirectActivity(MainActivity::class.java)
                    }
                )

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

    // Start the specified activity and finish this activity
    private fun startAndFinish(activity: Class<*>) {
        startActivity(Intent(this, activity))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleActivityResult(requestCode, resultCode, data)
    }

}