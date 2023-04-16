package com.daniel.ping.data.repositories

import com.daniel.ping.data.remote.getAllUsers
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.repositories.UserDataRepository
import com.daniel.ping.domain.utilities.CallHandler
import com.daniel.ping.domain.utilities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
): UserDataRepository {

    /**
     * Override function the retrieves a list of user objects via the Firebase FireStore
     * @param currentId the ID of the current user
     * @return a Resource object containing the retrieved list of User objects
     */
    override suspend fun getAllUsers(currentId: String): Resource<ArrayList<User>> =
        CallHandler.callHandler { fireStore.getAllUsers(currentId) }
}