package com.daniel.ping.data.repositories

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.daniel.ping.data.local.SharedPreferenceManager
import com.daniel.ping.data.remote.FacebookAuthManager
import com.daniel.ping.data.remote.firebaseService.getUserData
import com.daniel.ping.data.remote.firebaseService.insertEmail
import com.daniel.ping.data.remote.firebaseService.insertProfileDescription
import com.daniel.ping.data.remote.firebaseService.saveProfileImage
import com.daniel.ping.data.remote.firebaseService.signInWithEmailAndPasswordM
import com.daniel.ping.data.remote.firebaseService.signUpWithEmailAndPassword
import com.daniel.ping.data.remote.firebaseService.userAlreadyRegistered
import com.daniel.ping.data.remote.firebaseService.withCredentials
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.useCases.AuthCredentialsUseCase
import com.daniel.ping.domain.utilities.CallHandler
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val prefs: SharedPreferenceManager,
    private val facebookAuthManager: FacebookAuthManager,
    private val fireStore: FirebaseFirestore,
    private val store: FirebaseStorage
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

    // Function to check if the user is registered
    override suspend fun userAlreadyRegistered(email: String): Resource<Boolean> =
        CallHandler.callHandler { fireStore.userAlreadyRegistered(email) }

    // Function to insert the email to the database
    override suspend fun insertEmail(email: HashMap<String, Any>): Resource<Task<DocumentReference>> =
        CallHandler.callHandler { fireStore.insertEmail(email) }

    // Function to get user data
    override suspend fun getUserData(email: String): Resource<User?> =
        CallHandler.callHandler { fireStore.getUserData(email) }

    // Functions to store and retrieve data from Shared Preferences
    override fun putStringToPrefs(key: String, value: String) = prefs.putString(key, value)
    override fun putBooleanToPrefs(key: String, value: Boolean) = prefs.putBoolean(key, value)
    override fun getString(key: String): String = prefs.getString(key)
    override fun getBooleanToPrefs(key: String): Boolean = prefs.getBoolean(key)
    override fun cleanPrefs() = prefs.clean()

    /**
     * Inserts the user profile description into Firestore along with the profile image, and updates the profile image URL in shared preferences
     * @param userInformation The HashMap containing the user profile information
     * @param documentId The document ID in Firestore where the profile description will be inserted
     * @param profileImage The Uri of the profile image to be uploaded and associated the profile description
     * @return A Resource object wrapping the Task<Void> the represents the insertion operation
     */
    override suspend fun insertProfileDescription(userInformation: HashMap<String, Any>, documentId: String, profileImage: Uri): Resource<Task<Void>> =
        CallHandler.callHandler {
            val urlProfileImage = store.saveProfileImage(profileImage)
            prefs.putString(Constants.KEY_PROFILE_IMAGE_URL, urlProfileImage)
            userInformation[Constants.KEY_PROFILE_IMAGE_URL] = urlProfileImage
            fireStore.insertProfileDescription(userInformation, documentId)
        }


}