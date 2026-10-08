package com.compose.userlistapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class UserDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ------------------------------------------------------------------------
    // Standard / Happy Path Test
    // ------------------------------------------------------------------------

    @Test
    fun userDetailsScreen_displaysHeaderAndCorrectUserId() {
        val testUserId = 42

        composeTestRule.setContent {
            UserDetailsScreen(userId = testUserId)
        }

        // Verify "User Details" title is displayed
        composeTestRule.onNodeWithText("User Details").assertIsDisplayed()

        // Verify "User ID: 42" is displayed
        composeTestRule.onNodeWithText("User ID: 42").assertIsDisplayed()
    }

    // ------------------------------------------------------------------------
    // Edge Cases & Boundary Value Tests
    // ------------------------------------------------------------------------

    @Test
    fun userDetailsScreen_handlesZeroUserId() {
        composeTestRule.setContent {
            UserDetailsScreen(userId = 0)
        }

        composeTestRule.onNodeWithText("User Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("User ID: 0").assertIsDisplayed()
    }

    @Test
    fun userDetailsScreen_handlesNegativeUserId() {
        val negativeUserId = -1

        composeTestRule.setContent {
            UserDetailsScreen(userId = negativeUserId)
        }

        composeTestRule.onNodeWithText("User Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("User ID: -1").assertIsDisplayed()
    }

    @Test
    fun userDetailsScreen_handlesMaxIntegerUserId() {
        val maxUserId = Int.MAX_VALUE // 2147483647

        composeTestRule.setContent {
            UserDetailsScreen(userId = maxUserId)
        }

        composeTestRule.onNodeWithText("User Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("User ID: $maxUserId").assertIsDisplayed()
    }

    @Test
    fun userDetailsScreen_handlesMinIntegerUserId() {
        val minUserId = Int.MIN_VALUE // -2147483648

        composeTestRule.setContent {
            UserDetailsScreen(userId = minUserId)
        }

        composeTestRule.onNodeWithText("User Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("User ID: $minUserId").assertIsDisplayed()
    }
}