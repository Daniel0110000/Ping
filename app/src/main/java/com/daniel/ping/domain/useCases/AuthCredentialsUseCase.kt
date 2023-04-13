package com.daniel.ping.domain.useCases

import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

// Function perform the actual authentication with the provided credentials
// It takes an AuthCredential and returns a Task<AuthResult> which represents the result of the authentication process.
class AuthCredentialsUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {
    suspend fun invoke(account: AuthCredential): Task<AuthResult>?{
        // Calls the insertCredentials function of the AuthenticationRepository and stores the result in a variable called auth.
        return when(val auth = authenticationRepository.insertCredentials(account)){
            // If the result os a success, return the data of the resource
            is Resource.Success -> auth.data
            // If the result is an error, return null
            is Resource.Error -> null
        }
    }
}