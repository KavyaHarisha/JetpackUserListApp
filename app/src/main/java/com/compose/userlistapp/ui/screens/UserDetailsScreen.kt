package com.compose.userlistapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserDetailsScreen(userId: Int){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "User Details",
            style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "User ID: $userId",
            style = MaterialTheme.typography.titleMedium)

    }
}