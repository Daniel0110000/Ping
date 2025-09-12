package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.models.PresenceData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

fun JsonObject.toPresenceData(): PresenceData = Json.decodeFromJsonElement<PresenceData>(this)