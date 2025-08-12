package dev.dr10.ping.data.mappers

import dev.dr10.ping.data.models.UserData
import dev.dr10.ping.data.models.UserProfileData

fun UserData.toProfileData(): UserProfileData = UserProfileData(
    userId = user_id,
    username = username,
    bio = bio,
    profileImagePath = "",
    profileImageName = profile_image
)