package com.daniel.ping.ui.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.daniel.ping.domain.models.ValidationResult
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.useCases.AuthCredentialsUseCase
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.Resource
import com.daniel.ping.domain.utilities.SecurityService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    authCredentialsUseCase: AuthCredentialsUseCase,
    private val application: Application
) : AuthViewModelBase(authenticationRepository, authCredentialsUseCase, application) {

    // Function to initiate the sign-up process
    fun signUp(){
        // Validate email and password fields
        val validation = validateEmailAndPassword()

        // If validation fails, set error message and stop loading
        if(!validation.isValid){
            setMessage(validation.message)
            setIsLoading(false)
            return
        }

        // Set loading state to true
        setIsLoading(true)

        // Initialize sign-up process in IO thread
        viewModelScope.launch(Dispatchers.IO) {
            // Call authenticationRepository to sign-up with email and password
            val signUpResult = authenticationRepository.signUpWithEmailAndPassword(
                email = state.value.email,
                password = state.value.password
            )

            // Handle result based on success or error
            when (signUpResult){
                is Resource.Success -> handleSignUpSuccess(signUpResult.data!!)
                is Resource.Error -> handlerError(signUpResult.message.toString())
            }
        }

    }

    // Function to validate email and password fields
    private fun validateEmailAndPassword(): ValidationResult {
        return SecurityService.emailAndPasswordValidation(
            email = state.value.email,
            password = state.value.password,
            context = application.applicationContext
        )
    }

    // Function to handle successful sing-up
    private fun handleSignUpSuccess(result: Task<AuthResult>){
        // If sing-up is successful, update preference and registration status, stop loading
        result.addOnSuccessListener {
            authenticationRepository.putBooleanToPrefs(Constants.KEY_IS_SIGNED_IN, true)
            setRegistrationCompleted(true)
            setIsLoading(false)
        }
        // If sign-up fails, set error message and stop loading
        result.addOnFailureListener { e ->
            setMessage(e.message.toString())
            setIsLoading(false)
        }
    }

}