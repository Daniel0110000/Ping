package dev.dr10.ping.data.repositories

import dev.dr10.ping.data.local.datastore.UserProfileStore
import dev.dr10.ping.data.mappers.toUserData
import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.repositories.ProfileSetupRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class ProfileSetupRepositoryImpl(
    private val supabaseService: SupabaseClient,
    private val userProfileStore: UserProfileStore
): ProfileSetupRepository {

    /**
     * Saves the user profile data to the Supabase database and local datastore
     *
     * @param data The UserProfileData containing user information
     */
    override suspend fun saveProfile(data: UserProfileData) {
        supabaseService.from(Constants.USERS_TABLE).insert(data.toUserData())
        userProfileStore.saveProfileData(data)
    }
}