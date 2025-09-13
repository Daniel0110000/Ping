package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.data.mappers.toEntity
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitializeRealtimeNewMessagesUseCase(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(): Result<Boolean, ErrorType> = try {
        // Subscribe to the new messages channel
        val newMessageResponse = messagesRepository.subscribeAndListenNewMessages()

        // Insert new messages into the local database for display in the UI
        CoroutineScope(Dispatchers.IO).launch {
            newMessageResponse.collect { message -> messagesRepository.insertNewMessage(message.toEntity()) }
        }

        Result.Success(true)
    } catch (e: Exception) {
        Log.e(this::class.java.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}