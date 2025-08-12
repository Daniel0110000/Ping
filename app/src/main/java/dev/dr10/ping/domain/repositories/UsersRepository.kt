package dev.dr10.ping.domain.repositories

import dev.dr10.ping.data.models.UserProfileData

interface UsersRepository {
    suspend fun saveUserData(data: UserProfileData)

    suspend fun fetchUserData(userId: String): UserProfileData?
}