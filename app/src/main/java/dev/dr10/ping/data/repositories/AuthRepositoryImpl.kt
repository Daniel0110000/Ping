package dev.dr10.ping.data.repositories

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dev.dr10.ping.BuildConfig
import dev.dr10.ping.domain.repositories.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.user.UserSession

class AuthRepositoryImpl(
    private val authService: Auth
): AuthRepository {

    /**
     * Signs up user with email and password
     *
     * @param email The user's email
     * @param password The user's password
     */
    override suspend fun sigUpWithEmailAndPassword(email: String, password: String) {
        authService.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * Authenticates user with Google
     *
     * @param context The current activity context
     */
    override suspend fun authWithGoogle(context: Context) {
        val googleCredentials = getGoogleCredentials(context)
        authService.signInWith(IDToken) {
            idToken = googleCredentials.idToken
            provider = Google
        }
    }

    /**
     * Retrieves the current user session
     *
     * @return UserSession if available, null otherwise
     */
    override suspend fun getCurrentSession(): UserSession? = authService.currentSessionOrNull()

    /**
     * Retrieves Google credentials using the CredentialManager
     *
     * @param context The current activity context
     * @return GoogleIdTokenCredential containing the ID token
     */
    private suspend fun getGoogleCredentials(context: Context): GoogleIdTokenCredential {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = context
        )

        return GoogleIdTokenCredential.createFrom(result.credential.data)
    }
}