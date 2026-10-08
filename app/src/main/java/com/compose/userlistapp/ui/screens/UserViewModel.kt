package com.compose.userlistapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.userlistapp.data.respository.UserRepository
import com.compose.userlistapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<UserEvent>()
    val event: SharedFlow<UserEvent> = _event.asSharedFlow()

    init {
        loadUsers()
    }

    fun loadUsers(){
        viewModelScope.launch {
            _uiState.update { it.copy( isLoading = true, error = null) }
            try {
                val users = userRepository.getUsers()
                _uiState.update { it.copy(users = users, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Something went wrong", isLoading = false) }
            }
        }
    }

    fun onUserClick(user: User){
        viewModelScope.launch {
            _event.emit(UserEvent.NavigateToUserDetails(user.id))
        }
    }
}