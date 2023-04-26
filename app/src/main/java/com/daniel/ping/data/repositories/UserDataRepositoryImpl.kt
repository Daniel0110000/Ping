package com.daniel.ping.data.repositories

import com.daniel.ping.data.local.SharedPreferenceManager
import com.daniel.ping.data.remote.firebaseService.getAllUsers
import com.daniel.ping.data.remote.firebaseService.updateToken
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.repositories.UserDataRepository
import com.daniel.ping.domain.utilities.CallHandler
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val prefs: SharedPreferenceManager
): UserDataRepository {

    /**
     * Override function the retrieves a list of user objects via the Firebase FireStore
     * @param currentId the ID of the current user
     * @return a Resource object containing the retrieved list of User objects
     */
    override suspend fun getAllUsers(currentId: String): Resource<ArrayList<User>> =
        CallHandler.callHandler { fireStore.getAllUsers(currentId) }

    /**
     * Override function that updates the Firebase Cloud Messaging (FCM) token of the current user
     * @param token the new FCM token
     * @return a Resource object containing the result of the update operation
     */
    override suspend fun updateToken(token: String): Resource<Task<Void>> =
        CallHandler.callHandler { fireStore.updateToken(prefs.getString(Constants.KEY_USER_ID), token) }
}