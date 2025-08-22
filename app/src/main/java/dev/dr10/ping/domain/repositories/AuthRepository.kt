package dev.dr10.ping.domain.repositories

import android.content.Context
import dev.dr10.ping.data.models.UserProfileData
import io.github.jan.supabase.auth.user.UserSession

interface AuthRepository {
    suspend fun sigUpWithEmailAndPassword(email: String, password: String)

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun authWithGoogle(context: Context)

    suspend fun getCurrentSession(): UserSession?

    suspend fun getProfileData(): UserProfileData?

    suspend fun loginCompleted()

    suspend fun isUserLoggedIn(): Boolean
    suspend fun isProfileSetupCompleted(): Boolean

    suspend fun localSaveProfileData(data: UserProfileData)

}