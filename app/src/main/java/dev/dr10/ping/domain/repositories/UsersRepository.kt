package dev.dr10.ping.domain.repositories

import dev.dr10.ping.data.models.SimpleUserData
import dev.dr10.ping.data.models.UserProfileData

interface UsersRepository {
    suspend fun saveUserData(data: UserProfileData)

    suspend fun fetchUserData(userId: String): UserProfileData?

    suspend fun fetchSuggestedUsers(userId: String): List<UserProfileData>

    suspend fun fetchUsersByUsername(id: String, query: String): List<UserProfileData>

    suspend fun fetchSimpleUserData(userId: String): SimpleUserData
}