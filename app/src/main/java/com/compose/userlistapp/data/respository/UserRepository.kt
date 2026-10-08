package com.compose.userlistapp.data.respository

import com.compose.userlistapp.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}