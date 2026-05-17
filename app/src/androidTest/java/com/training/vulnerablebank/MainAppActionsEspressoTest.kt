package com.training.vulnerablebank

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainAppActionsEspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun loginAndOpenTransactions() {
        ensureLoggedInAndOnDashboard()

        composeTestRule
            .onNodeWithContentDescription("view_transactions_button", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Recent Transactions", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun loginAndOpenTransfer() {
        ensureLoggedInAndOnDashboard()

        composeTestRule
            .onNodeWithContentDescription("make_transfer_button", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("submit_transfer_button", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    private fun ensureLoggedInAndOnDashboard() {
        val loginButton = composeTestRule.onAllNodesWithContentDescription(
            label = "login_button",
            useUnmergedTree = true
        )

        if (loginButton.fetchSemanticsNodes().isNotEmpty()) {
            loginButton.onFirst().performClick()
        }

        composeTestRule
            .onNodeWithContentDescription("view_transactions_button", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}