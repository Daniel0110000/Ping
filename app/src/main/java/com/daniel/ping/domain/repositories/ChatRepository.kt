package com.daniel.ping.domain.repositories

import com.daniel.ping.domain.models.Chat

interface ChatRepository {

    suspend fun sendMessage(message: HashMap<String, Any>)

    fun listenerMessages(
        userId: String,
        receiverUserId: String,
        callback: (ArrayList<Chat>) -> Unit
    )

}