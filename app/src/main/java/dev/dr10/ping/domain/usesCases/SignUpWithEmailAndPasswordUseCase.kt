package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.utils.AuthDataValidator
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import io.appwrite.exceptions.AppwriteException

class SignUpWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): Result<Boolean, ErrorType> {
        // Check if email and password are valid
        AuthDataValidator.isValidAuthData(email, password).takeIf { it is Result.Error }?.let { return it }

        return try {
            // Creates a new user with the provided email and password
            authRepository.sigUpWithEmailAndPassword(email, password)
            // Logs in the user created in the previous step
            authRepository.signInWithEmailAndPassword(email,  password)
            Result.Success(true)
        } catch (e: AppwriteException) {
            Log.e("[ - ] SignUpWithEmailAndPasswordUseCase", e.message.toString())
            when (e.code) {
                409 -> Result.Error(ErrorType.USER_ALREADY_EXISTS)
                else -> Result.Error(ErrorType.UNKNOWN_ERROR)
            }
        }
    }

}