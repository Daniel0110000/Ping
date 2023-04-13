package com.daniel.ping.data.repositories

import android.app.Activity
import android.content.Intent
import com.daniel.ping.data.local.SharedPreferenceManager
import com.daniel.ping.data.remote.FacebookAuthManager
import com.daniel.ping.data.remote.signInWithEmailAndPasswordM
import com.daniel.ping.data.remote.signUpWithEmailAndPassword
import com.daniel.ping.data.remote.withCredentials
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.useCases.AuthCredentialsUseCase
import com.daniel.ping.domain.utilities.CallHandler
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val prefs: SharedPreferenceManager,
    private val facebookAuthManager: FacebookAuthManager
) : AuthenticationRepository {

    // Function to sign up with email and password
    override suspend fun signUpWithEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>> =
        CallHandler.callHandler { auth.signUpWithEmailAndPassword(email, password) }

    // Function to sign in with email and password
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>> =
        CallHandler.callHandler { auth.signInWithEmailAndPasswordM(email, password) }

    // Function to authenticate with a credential
    override suspend fun insertCredentials(credential: AuthCredential): Resource<Task<AuthResult>> =
        CallHandler.callHandler { auth.withCredentials(credential) }

    // Function to handle Facebook authentication
    override fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        facebookAuthManager.onActivityResult(requestCode, resultCode, data)

    // Function to perform Facebook authentication
    override suspend fun facebookAuth(activity: Activity, authCredentialsUseCase: AuthCredentialsUseCase): Task<AuthResult>? =
        facebookAuthManager.performSignIn(activity, authCredentialsUseCase)

    // Functions to store and retrieve data from Shared Preferences
    override fun putStringToPrefs(key: String, value: String) = prefs.putString(key, value)
    override fun putBooleanToPrefs(key: String, value: Boolean) = prefs.putBoolean(key, value)
    override fun getString(key: String): String = prefs.getString(key)
    override fun getBooleanToPrefs(key: String): Boolean = prefs.getBoolean(key)
    override fun cleanPrefs() = prefs.clean()
}