package dev.dr10.ping.domain.utils

sealed interface Error
typealias RootError = Error

sealed interface Result<out B, out E: RootError> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>
}