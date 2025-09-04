package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.models.RecentConversationData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

fun JsonObject.toRecentConversationData(): RecentConversationData = Json.decodeFromJsonElement<RecentConversationData>(this)