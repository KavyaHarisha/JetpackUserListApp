package com.compose.userlistapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.compose.userlistapp.model.User
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UserListContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ------------------------------------------------------------------------
    // Loading & Error States
    // ------------------------------------------------------------------------

    @Test
    fun loadingState_displaysLoadingIndicator() {
        val state = UserUiState(isLoading = true)

        composeTestRule.setContent {
            UserListContent(
                state = state,
                onUserClick = {}
            )
        }

        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        val errorMessage = "Failed to load users"
        val state = UserUiState(isLoading = false, error = errorMessage)

        composeTestRule.setContent {
            UserListContent(
                state = state,
                onUserClick = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithTag("loading").assertDoesNotExist()
    }

    // ------------------------------------------------------------------------
    // Success & List Rendering
    // ------------------------------------------------------------------------

    @Test
    fun successState_displaysUserList() {
        val users = listOf(
            User(id = 1, name = "John Doe", email = "john@example.com"),
            User(id = 2, name = "Jane Smith", email = "jane@example.com")
        )
        val state = UserUiState(isLoading = false, users = users)

        composeTestRule.setContent {
            UserListContent(
                state = state,
                onUserClick = {}
            )
        }

        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("john@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jane Smith").assertIsDisplayed()
        composeTestRule.onNodeWithText("jane@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loading").assertDoesNotExist()
    }

    // ------------------------------------------------------------------------
    // Edge Cases
    // ------------------------------------------------------------------------

    @Test
    fun emptyUsersList_displaysNoUsersAndNoLoadingIndicator() {
        val state = UserUiState(isLoading = false, users = emptyList(), error = null)

        composeTestRule.setContent {
            UserListContent(
                state = state,
                onUserClick = {}
            )
        }

        composeTestRule.onNodeWithTag("loading").assertDoesNotExist()
    }

    @Test
    fun longUserName_displaysCorrectlyWithoutCrashing() {
        val longName = "Super Long User Name ".repeat(5)
        val user = User(id = 99, name = longName, email = "long@example.com")
        val state = UserUiState(isLoading = false, users = listOf(user))

        composeTestRule.setContent {
            UserListContent(
                state = state,
                onUserClick = {}
            )
        }

        composeTestRule.onNodeWithText(longName).assertIsDisplayed()
    }

    // ------------------------------------------------------------------------
    // User Interaction Tests
    // ------------------------------------------------------------------------

    @Test
    fun userItemClick_triggersOnUserClickCallbackWithCorrectUser() {
        val user = User(id = 1, name = "John Doe", email = "john@example.com")
        val state = UserUiState(isLoading = false, users = listOf(user))
        var clickedUser: User? = null

        composeTestRule.setContent {
            UserListContent(
                state = state,
                onUserClick = { selectedUser -> clickedUser = selectedUser }
            )
        }

        composeTestRule.onNodeWithText("John Doe").performClick()

        assertEquals(user, clickedUser)
    }
}