package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.models.UserData
import dev.dr10.ping.data.models.UserProfileData

fun UserProfileData.toUserData(): UserData = UserData(
    user_id = userId,
    username = username,
    bio = bio,
    profile_image = profileImageName,
    last_connected = "now()",
    is_online = true,
    fcm_token = fcmToken
)