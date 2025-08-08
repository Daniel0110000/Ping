package dev.dr10.ping.domain.repositories

import android.content.Context

interface AuthRepository {
    suspend fun sigUpWithEmailAndPassword(email: String, password: String)

    suspend fun authWithGoogle(context: Context)

}