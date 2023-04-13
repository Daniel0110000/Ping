package com.daniel.ping.domain.utilities

class CallHandler {
    companion object{
        suspend fun <T> callHandler(block: suspend () -> T): Resource<T>{
            return try{
                Resource.Success(
                    data = block()
                )
            } catch (e: Exception){
                Resource.Error(
                    message = "Exception ${e.message}"
                )
            }
        }
    }
}