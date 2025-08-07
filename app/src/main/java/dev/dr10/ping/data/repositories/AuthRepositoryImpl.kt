package dev.dr10.ping.data.repositories

import dev.dr10.ping.domain.repositories.AuthRepository
import io.appwrite.ID
import io.appwrite.services.Account

class AuthRepositoryImpl(
    private val accountService: Account
): AuthRepository {

    override suspend fun sigUpWithEmailAndPassword(email: String, password: String) {
        accountService.create(userId = ID.unique(), email = email, password = password)
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        val session = accountService.createEmailPasswordSession(email = email, password = password)
    }
}