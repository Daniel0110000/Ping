package dev.dr10.ping.domain.utils

import android.net.Uri
import android.util.Patterns

object AuthDataValidator {
    fun isValidAuthData(email: String, password: String): Result<Boolean, ErrorType> = when {
        email.isBlank() || password.isBlank() -> Result.Error(ErrorType.EMPTY_FIELDS)
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Result.Error(ErrorType.INVALID_EMAIL)
        password.length < 6 -> Result.Error(ErrorType.PASSWORD_TOO_SHORT)
        else -> Result.Success(true)
    }

    fun isValidProfileSetupData(
        profileImageUri: Uri?,
        username: String,
        bio: String
    ): Result<Boolean, ErrorType> = when {
        username.isBlank() || bio.isBlank() -> Result.Error(ErrorType.EMPTY_FIELDS)
        profileImageUri == null -> Result.Error(ErrorType.EMPTY_PROFILE_IMAGE)
        bio.length > 80 -> Result.Error(ErrorType.BIO_TOO_LONG)
        else -> Result.Success(true)
    }
}