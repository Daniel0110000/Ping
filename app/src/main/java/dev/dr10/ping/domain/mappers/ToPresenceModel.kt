package dev.dr10.ping.domain.mappers

import dev.dr10.ping.data.models.PresenceData
import dev.dr10.ping.domain.models.PresenceModel
import dev.dr10.ping.domain.utils.MessageUtils

fun PresenceData.toModel(): PresenceModel = PresenceModel(
    userId = user_id,
    isOnline = is_online,
    lastConnected =  MessageUtils.formatLastConnected(last_connected)
)