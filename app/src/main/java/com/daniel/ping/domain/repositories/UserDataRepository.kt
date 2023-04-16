package com.daniel.ping.domain.repositories

import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task

interface UserDataRepository {

    suspend fun getAllUsers(currentId: String): Resource<ArrayList<User>>

    suspend fun updateToken(token: String): Resource<Task<Void>>

}