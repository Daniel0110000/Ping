package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.models.MessageData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

fun JsonObject.toMessageData(): MessageData = Json.decodeFromJsonElement<MessageData>(this)