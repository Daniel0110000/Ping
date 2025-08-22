package dev.dr10.ping.ui.extensions

import dev.dr10.ping.domain.utils.Result
import dev.dr10.ping.domain.utils.RootError

inline fun <B, E: RootError> Result<B, E>.onSuccess(action: (B) -> Unit): Result<B, E> {
    if (this is Result.Success) action(data)
    return this
}