package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.models.NotificationRequestBody
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun Map<String, String>.toDataClass(): NotificationRequestBody {
    val filterMap = this - "token"
    val jsonString = json.encodeToString(filterMap)
    return json.decodeFromString(jsonString)
}