package dev.dr10.ping.domain.utils

enum class ErrorType: Error {
    INVALID_EMAIL,
    EMPTY_FIELDS,
    PASSWORD_TOO_SHORT,
    USER_ALREADY_EXISTS,
    USER_CANCELLED_AUTH,
    UNKNOWN_ERROR,
}