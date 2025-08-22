package dev.dr10.ping.ui.extensions

import dev.dr10.ping.domain.utils.Result
import dev.dr10.ping.domain.utils.RootError

inline fun <B, E: RootError> Result<B, E>.onError(action: (E) -> Unit): Result<B, E> {
    if (this is Result.Error) action(error)
    return this
}