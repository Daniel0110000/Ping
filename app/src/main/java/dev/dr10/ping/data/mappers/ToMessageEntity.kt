package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.models.MessageData

fun MessageData.toEntity(): MessageEntity = MessageEntity(
    id = this.id,
    senderId = this.sender_id,
    receiverId = this.receiver_id,
    content = this.content,
    createdAt = this.created_at
)