package dev.dr10.ping.domain.repositories

interface AuthRepository {
    suspend fun sigUpWithEmailAndPassword(email: String, password: String)
    suspend fun signInWithEmailAndPassword(email: String, password: String)
}