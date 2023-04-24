package com.daniel.ping.domain.utilities

fun <T> Resource<T>.handleResult(
    onSuccess: (T) -> Unit,
    onError: (Resource.Error<T>) -> Unit
){
    when (this){
        is Resource.Success -> data?.let { onSuccess(it) }
        is Resource.Error -> onError(this)
    }
}