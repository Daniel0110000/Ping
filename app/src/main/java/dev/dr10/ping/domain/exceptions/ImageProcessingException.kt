package dev.dr10.ping.domain.exceptions

import dev.dr10.ping.domain.utils.ErrorType

class ImageProcessingException(
    val errorType: ErrorType,
    message: String? = null,
    cause: Throwable? = null
): Exception(message, cause)