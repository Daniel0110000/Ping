package dev.dr10.ping.domain.repositories

import dev.dr10.ping.data.models.MessageData

interface MessagesRepository {

    suspend fun sendMessage(message: MessageData)

}