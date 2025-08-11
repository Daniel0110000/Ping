package dev.dr10.ping.domain.repositories

import android.content.Context
import io.github.jan.supabase.auth.user.UserSession

interface AuthRepository {
    suspend fun sigUpWithEmailAndPassword(email: String, password: String)

    suspend fun authWithGoogle(context: Context)

    suspend fun getCurrentSession(): UserSession?

    suspend fun loginCompleted()

    suspend fun isUserLoggedIn(): Boolean
    suspend fun isProfileSetupCompleted(): Boolean

}