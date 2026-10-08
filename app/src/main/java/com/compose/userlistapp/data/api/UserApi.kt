package com.compose.userlistapp.data.api

import com.compose.userlistapp.model.User
import retrofit2.http.GET

interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<User>
}