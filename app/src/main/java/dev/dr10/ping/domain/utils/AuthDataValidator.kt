package dev.dr10.ping.domain.utils

import android.util.Patterns

object AuthDataValidator {
    fun isValidAuthData(email: String, password: String): Result<Boolean, ErrorType> {
        if (email.isBlank() || password.isBlank()) return Result.Error(ErrorType.EMPTY_FIELDS)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return Result.Error(ErrorType.INVALID_EMAIL)
        if (password.length < 8) return Result.Error(ErrorType.PASSWORD_TOO_SHORT)
        return Result.Success(true)
    }
}