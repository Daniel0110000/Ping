package dev.dr10.ping.domain.repositories

import dev.dr10.ping.data.models.UserProfileData

interface ProfileSetupRepository {
    suspend fun saveProfile(data: UserProfileData)
}