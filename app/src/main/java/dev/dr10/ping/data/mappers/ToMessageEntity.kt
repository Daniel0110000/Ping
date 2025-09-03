package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.domain.utils.MessageUtils

fun MessageData.toEntity(): MessageEntity = MessageEntity(
    id = this.id,
    chatId = this.chat_id,
    senderId = this.sender_id,
    receiverId = this.receiver_id,
    content = this.content,
    date = MessageUtils.convertUtcToLocalTime(this.created_at),
    createdAt = this.created_at
)