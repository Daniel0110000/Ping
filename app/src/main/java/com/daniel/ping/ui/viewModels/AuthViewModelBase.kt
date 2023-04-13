package com.daniel.ping.ui.viewModels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.R
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.useCases.AuthCredentialsUseCase
import com.daniel.ping.domain.utilities.Constants
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Definition of the AuthViewModelBase class as a subclass of ViewModel
open class AuthViewModelBase(
    private val authenticationRepository: AuthenticationRepository,
    private val authCredentialsUseCase: AuthCredentialsUseCase,
    private val application: Application
) : ViewModel() {

    // Mutable state flow to hold the authentication state
    private val _state = MutableStateFlow(AuthState())
    // Exposed state flow of the authentication state
    val state = _state.asStateFlow()

    // Initialize email and password with empty string
    init {
        setEmailAndPassword("", "")
    }

    // Data class to hold the authentication state
    data class AuthState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val message: String = "",
        val registrationCompleted: Boolean = false
    )

    // Function to handle result of a Google sign-in task
    fun handleResults(task: Task<GoogleSignInAccount>, context: Context){
        // Check if the task was successful
        if(task.isSuccessful){
            // Get the signed-in account
            val account: GoogleSignInAccount? = task.result
            if(account != null){
                // Get the authentication credential
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                // Use the credentials use case to authenticate with Google
                viewModelScope.launch(Dispatchers.IO) {
                    val google = authCredentialsUseCase.invoke(credential)
                    google?.apply {
                        addOnSuccessListener {
                            // Set the user as signed in and registration as completed
                            authenticationRepository.putBooleanToPrefs(Constants.KEY_IS_SIGNED_IN, true)
                            setRegistrationCompleted(true)

                        }
                        addOnFailureListener { e -> setMessage(e.message.toString()) }
                    }
                }
            } else setMessage(context.getString(R.string.errorMessage))
        } else setMessage(task.exception!!.message.toString())
    }

    // Function to authentication with Facebook
    fun withFacebook(activity: Activity){
        viewModelScope.launch(Dispatchers.IO) {
            // Use the authentication repository to authenticate with Facebook
            val result = authenticationRepository.facebookAuth(activity, AuthCredentialsUseCase(authenticationRepository))
            result?.apply {
                addOnSuccessListener {
                    // Set the user as signed in and registration as completed
                    authenticationRepository.putBooleanToPrefs(Constants.KEY_IS_SIGNED_IN, true)
                    setRegistrationCompleted(true)
                }
                addOnFailureListener { setMessage(application.getString(R.string.errorMessage)) }
            }
        }
    }

    // Function to handle activity results for authentication
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        authenticationRepository.handleActivityResult(requestCode, resultCode, data)

    // Function to handle authentication errors
    fun handlerError(errorMessage: String){
        setMessage(errorMessage)
        setIsLoading(false)
    }

    fun setEmailAndPassword(email: String, password: String){
        viewModelScope.launch {
            _state.update { it.copy(
                email = email,
                password = password
            )}
        }
    }

    fun setMessage(text: String){
        viewModelScope.launch {
            _state.update { it.copy(
                message = text
            ) }
        }
    }

    fun setRegistrationCompleted(value: Boolean){
        viewModelScope.launch {
            _state.update { it.copy(
                registrationCompleted = value
            ) }
        }
    }

    fun setIsLoading(value: Boolean){
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = value
            ) }
        }
    }

}