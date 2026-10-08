package com.compose.userlistapp.ui.screens

import com.compose.userlistapp.model.User

data class UserUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)

sealed class UserEvent {
    data class NavigateToUserDetails(val userId: Int) : UserEvent()
}
