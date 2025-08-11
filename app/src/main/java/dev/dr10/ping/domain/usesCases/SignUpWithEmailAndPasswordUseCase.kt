package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.utils.AuthDataValidator
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class SignUpWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): Result<Boolean, ErrorType> {
        // Check if email and password are valid
        AuthDataValidator.isValidAuthData(email, password).takeIf { it is Result.Error }?.let { return it }

        return try {
            // Creates a new user with the provided email and password
            authRepository.sigUpWithEmailAndPassword(email, password)
            // Marks the login as completed after successful authentication
            authRepository.loginCompleted()
            Result.Success(true)
        } catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.message.toString())
            if (e.message.orEmpty().contains(Constants.ERROR_USER_ALREADY_EXISTS, ignoreCase = true)) Result.Error(ErrorType.USER_ALREADY_EXISTS)
            else Result.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

}