package dev.dr10.ping.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * DataStore for managing user profile data
 *
 * @param context Application context to access DataStore
 */
class UserProfileStore (
    private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.USER_PROFILE_PREFS)
        private val USER_ID = stringPreferencesKey(Constants.USER_PROFILE_ID)
        private val USERNAME = stringPreferencesKey(Constants.USER_PROFILE_USERNAME)
        private val BIO = stringPreferencesKey(Constants.USER_PROFILE_BIO)
        private val PROFILE_IMAGE_PATH = stringPreferencesKey(Constants.USER_PROFILE_IMAGE_PATH)
        private val IS_LOGGED_IN = booleanPreferencesKey(Constants.IS_LOGGED_IN)
        private val IS_PROFILE_SETUP_COMPLETED = booleanPreferencesKey(Constants.IS_PROFILE_SETUP_COMPLETED)
    }

    suspend fun loginCompleted() { context.dataStore.edit { preferences -> preferences[IS_LOGGED_IN] = true } }

    /**
     * Saves user profile data to DataStore
     *
     * @param profileData UserProfileData object containing user profile data
     */
    suspend fun saveProfileData(profileData: UserProfileData) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = profileData.userId
            preferences[USERNAME] = profileData.username
            preferences[BIO] = profileData.bio
            preferences[PROFILE_IMAGE_PATH] = profileData.profileImagePath
            preferences[IS_PROFILE_SETUP_COMPLETED] = true
        }
    }

    /**
     * Gets user profile data from DataStore
     *
     * @return UserProfileData object containing user profile data, or null if not found
     */
    suspend fun getProfileData(): UserProfileData? = context.dataStore.data.map { preferences ->
        val userId = preferences[USER_ID] ?: return@map null
        val username = preferences[USERNAME] ?: return@map null
        val bio = preferences[BIO] ?: return@map null
        val profileImagePath = preferences[PROFILE_IMAGE_PATH] ?: return@map null

        UserProfileData(
            userId = userId,
            username = username,
            bio = bio,
            profileImagePath = profileImagePath
        )
    }.firstOrNull()

    suspend fun isLoggedIn() = context.dataStore.data.map {
        preferences -> preferences[IS_LOGGED_IN] ?: false
    }.first()

    suspend fun isProfileSetupCompleted() = context.dataStore.data.map { preferences ->
        preferences[IS_PROFILE_SETUP_COMPLETED] ?: false
    }.first()
}