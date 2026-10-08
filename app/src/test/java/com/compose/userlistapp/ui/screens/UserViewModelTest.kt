package com.compose.userlistapp.ui.screens

import app.cash.turbine.test
import com.compose.userlistapp.MainDispatcherRule
import com.compose.userlistapp.data.respository.UserRepository
import com.compose.userlistapp.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userRepository: UserRepository = mockk()
    private lateinit var viewModel: UserViewModel

    private val sampleUsers = listOf(
        User(id = 1, name = "John Doe", email = "john@example.com"),
        User(id = 2, name = "Jane Smith", email = "jane@example.com")
    )

    // ------------------------------------------------------------------------
    // Happy Path Tests
    // ------------------------------------------------------------------------

    @Test
    fun `loadUsers success updates uiState with users and isLoading false`() = runTest {
        // Given
        coEvery { userRepository.getUsers() } returns sampleUsers

        // When
        viewModel = UserViewModel(userRepository)

        // Then
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertNull(currentState.error)
        assertEquals(sampleUsers, currentState.users)
        coVerify(exactly = 1) { userRepository.getUsers() }
    }

    // ------------------------------------------------------------------------
    // Error Handling & Edge Cases
    // ------------------------------------------------------------------------

    @Test
    fun `loadUsers failure updates uiState with exception message and isLoading false`() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery { userRepository.getUsers() } throws RuntimeException(errorMessage)

        // When
        viewModel = UserViewModel(userRepository)

        // Then
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertEquals(errorMessage, currentState.error)
        assertTrue(currentState.users.isEmpty())
        coVerify(exactly = 1) { userRepository.getUsers() }
    }

    @Test
    fun `loadUsers failure with null exception message sets fallback default error`() = runTest {
        // Given (Exception with no message, e.g. message == null)
        coEvery { userRepository.getUsers() } throws NullPointerException()

        // When
        viewModel = UserViewModel(userRepository)

        // Then
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertEquals("Something went wrong", currentState.error)
        assertTrue(currentState.users.isEmpty())
    }

    @Test
    fun `loadUsers returning empty list updates uiState with empty list and no error`() = runTest {
        // Given
        coEvery { userRepository.getUsers() } returns emptyList()

        // When
        viewModel = UserViewModel(userRepository)

        // Then
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertNull(currentState.error)
        assertTrue(currentState.users.isEmpty())
    }

    @Test
    fun `loadUsers retry after failure clears previous error and loads users successfully`() = runTest {
        // Given initial failure
        coEvery { userRepository.getUsers() } throws RuntimeException("Initial failure")
        viewModel = UserViewModel(userRepository)

        assertEquals("Initial failure", viewModel.uiState.value.error)

        // When retry succeeds
        coEvery { userRepository.getUsers() } returns sampleUsers
        viewModel.loadUsers()

        // Then
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertNull(currentState.error)
        assertEquals(sampleUsers, currentState.users)
        coVerify(exactly = 2) { userRepository.getUsers() }
    }

    // ------------------------------------------------------------------------
    // Event Emission Tests
    // ------------------------------------------------------------------------

    @Test
    fun `onUserClick emits NavigateToUserDetails event`() = runTest {
        // Given
        coEvery { userRepository.getUsers() } returns sampleUsers
        viewModel = UserViewModel(userRepository)

        val userToClick = sampleUsers.first()

        // When & Then (Testing SharedFlow using Turbine)
        viewModel.event.test {
            viewModel.onUserClick(userToClick)

            val emittedEvent = awaitItem()
            assertTrue(emittedEvent is UserEvent.NavigateToUserDetails)
            assertEquals(userToClick.id, (emittedEvent as UserEvent.NavigateToUserDetails).userId)
        }
    }
}