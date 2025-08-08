package dev.dr10.ping.domain.usesCases

import android.content.Context
import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class AuthWithGoogleUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(context: Context): Result<Boolean, ErrorType> = try {
        authRepository.authWithGoogle(context)
        Result.Success(true)
    } catch (e: Exception) {
        Log.e(this::class.java.simpleName, e.message.toString())
        if (e.message.orEmpty().contains(Constants.ERROR_CANCELLED_AUTH, ignoreCase = true)) Result.Error(ErrorType.USER_CANCELLED_AUTH)
        else Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}