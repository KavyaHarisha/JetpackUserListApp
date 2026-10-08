package com.compose.userlistapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compose.userlistapp.model.User

@Composable
fun UserListScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onNavigateToUserDetails: (Int) -> Unit
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when(event){
                is UserEvent.NavigateToUserDetails -> onNavigateToUserDetails(event.userId)
            }
        }
    }

    UserListContent(
        state = state,
        onUserClick = { user -> viewModel.onUserClick(user) }
    )

}

@Composable
fun UserListContent(
    state: UserUiState,
    onUserClick: (User) -> Unit
){
    when{
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator(modifier = Modifier.testTag("loading"))
            }
        }
        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Text(text = state.error,
                    color = MaterialTheme.colorScheme.error)
            }
            }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)){
                items(
                    items = state.users,
                    key = {it.id}
                ){
                    user ->
                    UserListItem(
                        user = user,
                        onUserClick = { onUserClick(user) }
                    )
                }

            }

        }
        }

}

@Composable
private fun UserListItem(
    user: User,
    onUserClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onUserClick)
            .padding(16.dp)
    ) {
        Text(text =  user.name,
            style = MaterialTheme.typography.titleMedium)
        Text(text = user.email,
            style = MaterialTheme.typography.bodyMedium)
    }
}