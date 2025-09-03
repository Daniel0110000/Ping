package dev.dr10.ping.domain.mappers

import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.domain.models.MessageModel

fun MessageEntity.toModel(): MessageModel = MessageModel(
    senderId = senderId,
    receiverId = receiverId,
    content = content,
    date = date
)