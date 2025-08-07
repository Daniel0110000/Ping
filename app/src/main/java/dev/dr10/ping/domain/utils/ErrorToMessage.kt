package dev.dr10.ping.domain.utils

import dev.dr10.ping.R

fun ErrorType.getErrorMessageId(): Int = when(this) {
    ErrorType.INVALID_EMAIL -> R.string.invalid_email
    ErrorType.EMPTY_FIELDS -> R.string.empty_fields
    ErrorType.PASSWORD_TOO_SHORT -> R.string.password_too_short
    ErrorType.USER_ALREADY_EXISTS -> R.string.user_already_exists
    ErrorType.UNKNOWN_ERROR -> R.string.unknown_error
}