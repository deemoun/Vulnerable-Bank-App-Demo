package com.training.vulnerablebank

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.training.vulnerablebank.utils.PreferencesManager
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainAppActionsEspressoTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private lateinit var scenario: ActivityScenario<LoginActivity>

    @Before
    fun launchApp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        PreferencesManager(context).resetToInitialState()
        scenario = ActivityScenario.launch(LoginActivity::class.java)
    }

    @After
    fun closeApp() {
        scenario.close()
    }

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

    @Test
    fun loginAndMakeTransfer() {
        ensureLoggedInAndOnDashboard()

        composeTestRule
            .onNodeWithContentDescription("make_transfer_button", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("recipientField", useUnmergedTree = true)
            .assertIsDisplayed()
            .performTextInput("lisa")

        composeTestRule
            .onNodeWithContentDescription("amountField", useUnmergedTree = true)
            .assertIsDisplayed()
            .performTextInput("100")

        composeTestRule
            .onNodeWithContentDescription("submit_transfer_button", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("transfer_status_message", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Transfer completed", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun loginAndOpenSettings() {
        ensureLoggedInAndOnDashboard()

        composeTestRule
            .onNodeWithContentDescription("settings_button", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("settings_heading", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("clear_all_data_button", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("network_connection_test_button", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("logout_button", useUnmergedTree = true)
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
