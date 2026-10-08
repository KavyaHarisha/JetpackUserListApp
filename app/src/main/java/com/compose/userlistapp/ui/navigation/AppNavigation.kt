package com.compose.userlistapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.userlistapp.ui.screens.UserDetailsScreen
import com.compose.userlistapp.ui.screens.UserListScreen
import com.compose.userlistapp.ui.screens.UserViewModel

@Composable
fun AppNavigation(){
    val navController =  rememberNavController()
    NavHost(navController = navController, startDestination = "user_list") {
        composable("user_list"){

            val userViewModel = hiltViewModel<UserViewModel>()
            UserListScreen(viewModel = userViewModel,
                onNavigateToUserDetails = {
                    userId -> navController.navigate("user_details/$userId")
                })
        }
        composable(
            route = "user_details/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ){backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            UserDetailsScreen(userId = userId)
        }

    }
}