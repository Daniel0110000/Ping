package dev.dr10.ping.domain.utils

import dev.dr10.ping.R

fun ErrorType.getErrorMessageId(): Int = when(this) {
    ErrorType.INVALID_EMAIL -> R.string.invalid_email
    ErrorType.EMPTY_FIELDS -> R.string.empty_fields
    ErrorType.EMPTY_PROFILE_IMAGE -> R.string.empty_profile_image
    ErrorType.PASSWORD_TOO_SHORT -> R.string.password_too_short
    ErrorType.USER_ALREADY_EXISTS -> R.string.user_already_exists
    ErrorType.USER_CANCELLED_AUTH -> R.string.user_cancelled_auth
    ErrorType.BIO_TOO_LONG -> R.string.bio_too_long
    ErrorType.PROCESSING_IMAGE -> R.string.processing_image
    ErrorType.USER_NOT_AUTHENTICATED -> R.string.user_not_authenticated
    ErrorType.UNKNOWN_ERROR -> R.string.unknown_error
}