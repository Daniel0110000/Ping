package dev.dr10.ping.domain.mappers

import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.models.UserProfileModel

fun UserProfileData.toModel(): UserProfileModel = UserProfileModel(
    userId = this.userId,
    username = this.username,
    bio = this.bio
)