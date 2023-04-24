package com.daniel.ping.domain.repositories

import android.app.Activity
import android.content.Intent
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.useCases.AuthCredentialsUseCase
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference

interface AuthenticationRepository {

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>>

    suspend fun insertCredentials(credential: AuthCredential): Resource<Task<AuthResult>>

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    suspend fun facebookAuth(activity: Activity, authCredentialsUseCase: AuthCredentialsUseCase): Task<AuthResult>?

    suspend fun userAlreadyRegistered(email: String): Resource<Boolean>

    suspend fun insertEmail(email: HashMap<String, Any>): Resource<Task<DocumentReference>>

    suspend fun getUserData(email: String): Resource<User?>

    fun putStringToPrefs(key: String, value: String)

    fun putBooleanToPrefs(key: String, value: Boolean)

    fun getString(key: String): String

    fun getBooleanToPrefs(key: String): Boolean

    fun cleanPrefs()

    suspend fun insertProfileDescription(description: HashMap<String, Any>, documentId: String): Resource<Task<Void>>

}