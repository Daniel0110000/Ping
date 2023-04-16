package com.daniel.ping.domain.repositories

import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.Resource

interface UserDataRepository {

    suspend fun getAllUsers(currentId: String): Resource<ArrayList<User>>

}