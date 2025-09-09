package dev.dr10.ping.domain.mappers

import dev.dr10.ping.data.models.UserStatusData
import dev.dr10.ping.domain.models.UserStatusModel
import dev.dr10.ping.domain.utils.MessageUtils

fun UserStatusData.toModel(): UserStatusModel = UserStatusModel(
    isOnline = is_online,
    lastConnected = MessageUtils.formatLastConnected(last_connected)
)