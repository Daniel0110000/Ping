package com.daniel.ping.ui.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.daniel.ping.R
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.useCases.AuthCredentialsUseCase
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    authCredentialsUseCase: AuthCredentialsUseCase,
    private val application: Application
) : AuthViewModelBase(authenticationRepository, authCredentialsUseCase, application){

    // Returns a boolean value that indicates whether the user is signed in or not,
    // ... by querying the value store in 'SharedPreferences' using a specific key
    fun isSignedIn(): Boolean =
        authenticationRepository.getBooleanToPrefs(Constants.KEY_IS_SIGNED_IN)

    // Returns a boolean value that indicates whether the user's profile is complete or not,
    // ... by querying the value stored in 'SharedPreferences' using another specific key
    fun isCompletedProfile(): Boolean =
        authenticationRepository.getBooleanToPrefs(Constants.KEY_IS_PROFILE_FULLY_COMPLETED)

    fun singIn(){
        // Checks that required fields are not empty
        if(!areFieldsValid()){
            // If any field is empty, shows an error message and sets 'isLoading' to 'false'
            setMessage(application.getString(R.string.emptyFieldsErrorMessage))
            setIsLoading(false)
            return
        }

        // If fields are valid, sets 'isLoading' to 'true'
        setIsLoading(true)

        // Execute the sing-in operation on a background thread
        viewModelScope.launch(Dispatchers.IO) {
            val signInResult = authenticationRepository.signInWithEmailAndPassword(
                email = state.value.email,
                password = state.value.password
            )

            // Handle the sing-in operation result based on its type
            when(signInResult){
                is Resource.Success -> handleSingInSuccess(signInResult.data!!)
                is Resource.Error -> handlerError(signInResult.message.toString())
            }
        }

    }

    // Function to validate fields
    private fun areFieldsValid(): Boolean {
        return state.value.email.isNotEmpty() && state.value.password.isNotEmpty()
    }

    // Function to handle successful sing-in
    private fun handleSingInSuccess(r1: Task<AuthResult>){
        r1.addOnSuccessListener {
            // If sign-in is success, update preference and registration status, stop loading
            authenticationRepository.putBooleanToPrefs(Constants.KEY_IS_SIGNED_IN, true)
            setRegistrationCompleted(true)
            setIsLoading(false)
        }
        // If sign-in fails, set error message and stop loading
        r1.addOnFailureListener { e ->
            setMessage(e.message.toString())
            setIsLoading(false)
        }
    }

}