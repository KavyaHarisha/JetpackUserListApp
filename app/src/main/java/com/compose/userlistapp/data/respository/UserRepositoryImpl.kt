package com.compose.userlistapp.data.respository

import com.compose.userlistapp.data.api.UserApi
import com.compose.userlistapp.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(val userApi: UserApi): UserRepository {
    override suspend fun getUsers(): List<User> {
        return userApi.getUsers()
    }
}