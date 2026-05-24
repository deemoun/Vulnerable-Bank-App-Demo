package com.training.vulnerablebank;

import static com.training.vulnerablebank.utils.TestAssertions.assertTextEqualsAny;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("VulnerableBank Mobile App")
@Feature("Core user flows")
public class MainAppTest extends TestBase {

    @DisplayName("User can log into the app")
    @Description("Verifies admin login and navigation to the transactions screen from the dashboard.")
    @Story("Login")
    @Owner("qa")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginAndOpenTransactions() {
        Allure.step("Login as admin user");
        loginPage.loginAsAdminAndWaitForDashboard();
        Allure.step("Open transactions and validate heading");
        assertTrue(dashboardPage.isTransactionsButtonVisible(), "Transactions navigation button is missing on the dashboard");
        dashboardPage.openTransactions();
        assertTrue(transactionsPage.isBackToDashboardButtonVisible(), "Back-to-dashboard button is missing on the transactions screen");
        assertTextEqualsAny(
                transactionsPage.getScreenHeadingText(),
                "Recent Transactions",
                "Последние транзакции"
        );
    }

    @DisplayName("User can transfer money to lisa")
    @Description("Verifies transfer from dashboard: admin sends an amount to lisa and receives confirmation.")
    @Story("Transfer")
    @Owner("qa")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginAndTransferToLisa() {
        Allure.step("Login and open transfer screen");
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Transfer button is missing on the dashboard");

        Allure.step("Submit transfer and validate success message");
        dashboardPage.clickTransferButton();
        transferPage.enterLisaTextInRecipientField();
        transferPage.enterAmountTextInAmountField();
        transferPage.clickSubmitTransferButton();

        assertTextEqualsAny(
                transferPage.getTransferSuccessToastText(),
                "Transfer completed",
                "Перевод выполнен"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"50.0", "100.0", "500.0"})
    @Description("Verifies transfers with different amounts and successful completion validation.")
    @Story("Transfer")
    public void loginAndMakeTransferWithDifferentAmounts(String amount) {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Transfer button is missing on the dashboard");

        dashboardPage.clickTransferButton();

        transferPage.enterLisaTextInRecipientField();
        transferPage.enterAmount(amount);
        transferPage.clickSubmitTransferButton();

        assertTextEqualsAny(
                transferPage.getTransferSuccessToastText(),
                "Transfer completed",
                "Перевод выполнен"
        );
    }

    @DisplayName("User can make a transfer")
    @Description("Basic transfer check after admin login.")
    @Story("Transfer")
    @Test
    public void loginAndMakeTransfer() {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Transfer button is missing on the dashboard");
        dashboardPage.clickTransferButton();
        transferPage.enterLisaTextInRecipientField();
        transferPage.enterAmountTextInAmountField();
        transferPage.clickSubmitTransferButton();
        assertTextEqualsAny(
                transferPage.getTransferSuccessToastText(),
                "Transfer completed",
                "Перевод выполнен"
        );
    }

    @DisplayName("User cannot transfer to a non-existing recipient")
    @Description("Negative check: transfer to a non-existing recipient should fail with a validation error.")
    @Story("Transfer")
    @Test
    public void loginAndFailTransferToUnknownRecipient() {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Transfer button is missing on the dashboard");

        dashboardPage.clickTransferButton();
        transferPage.enterRecipient("ghost-user");
        transferPage.enterAmount("50.0");
        transferPage.clickSubmitTransferButton();

        assertTextEqualsAny(
                transferPage.getTransferSuccessToastText(),
                "Recipient not found",
                "Получатель не найден"
        );
    }

    @DisplayName("User can switch language to Russian")
    @Description("Verifies changing app language to Russian from Settings.")
    @Story("Settings")
    @Test
    public void switchLanguageToRussianFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isRussianLanguageButtonVisible(), "Russian language switch button is missing");
        settingsPage.clickRussianLanguageButton();

        assertEquals("Настройки", settingsPage.getScreenHeadingText(), "Screen heading should be \"Настройки\" after switching to Russian");
    }

    @DisplayName("User can open settings")
    @Description("Verifies availability of core actions on the settings screen.")
    @Story("Settings")
    @Test
    public void loginAndOpenSettings(){
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isSettingsButtonVisible(), "Settings button is missing on the dashboard");
        dashboardPage.openSettings();
        assertTrue(settingsPage.isBackToDashboardButtonVisible(), "Back button (return to dashboard) is missing in settings");
        assertTrue(settingsPage.isClearAllDataButtonVisible(), "Clear data button is missing in settings");
        assertTrue(settingsPage.isNetworkConnectionTestButtonVisible(), "Network test button is missing in settings");
        assertTrue(settingsPage.isLogoutButtonVisible(), "Logout button is missing in settings");
        assertTextEqualsAny(
                settingsPage.getScreenHeadingText(),
                "Settings",
                "Настройки"
        );
    }


    @DisplayName("User can return to dashboard from settings")
    @Description("Verifies returning to dashboard through the back button in settings.")
    @Story("Dashboard")
    @Test
    public void openSettingsAndReturnToDashboard() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isBackToDashboardButtonVisible(), "Back button (return to dashboard) is missing in settings");
        settingsPage.clickBackToDashboardButton();

        assertTrue(dashboardPage.isTransferButtonVisible(), "Transfer button is missing on the dashboard");
    }

    @DisplayName("User can switch language back to English")
    @Description("Verifies language switching both ways: Russian -> English.")
    @Story("Settings")
    @Test
    public void switchLanguageBackToEnglishFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isRussianLanguageButtonVisible(), "Russian language switch button is missing");
        settingsPage.clickRussianLanguageButton();
        assertEquals("Настройки", settingsPage.getScreenHeadingText(), "Screen heading should be \"Настройки\" after switching to Russian");

        assertTrue(settingsPage.isEnglishLanguageButtonVisible(), "English language switch button is missing");
        settingsPage.clickEnglishLanguageButton();
        assertEquals("Settings", settingsPage.getScreenHeadingText(), "Screen heading should be Settings after switching to English");
    }

    @DisplayName("User can run network test from settings")
    @Description("Verifies running built-in network diagnostics and receiving a result.")
    @Story("Settings")
    @Test
    public void runNetworkConnectionTestFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isNetworkConnectionTestButtonVisible(), "Network test button is missing in settings");
        settingsPage.clickNetworkConnectionTestButton();

        String toastText = settingsPage.waitForNetworkTestToastText();
        assertTrue(toastText.contains("Network test successful")
                || toastText.contains("Сетевая проверка успешна"),
                "Network test result toast did not appear or has unexpected text: " + toastText);
    }


    @DisplayName("User can log out and log in again")
    @Description("Verifies logout/login flow for current user.")
    @Story("Login")
    @Test
    public void logoutAndLoginAgain() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();
        settingsPage.clickLogoutButton();

        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Transfer button is missing on the dashboard");
    }


    @DisplayName("Admin transfers $100 to lisa and lisa balance increases by $100")
    @Description("End-to-end scenario: transfer from admin to lisa and verify balance change after re-login.")
    @Story("Dashboard")
    @Test
    public void adminTransfer100ToLisaAndCheckLisaBalance() {
        Allure.step("Login as admin and verify initial balance");
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.getBalanceAmountText().contains("1,000")
                        || dashboardPage.getBalanceAmountText().contains("1000"),
                "Admin should have balance 1000 before transfer");

        Allure.step("Transfer 100$ from admin to lisa");
        dashboardPage.clickTransferButton();
        transferPage.enterLisaTextInRecipientField();
        int transferAmount = 100;
        transferPage.enterAmount(String.valueOf(transferAmount));
        transferPage.clickSubmitTransferButton();

        assertTextEqualsAny(
                transferPage.getTransferSuccessToastText(),
                "Transfer completed",
                "Перевод выполнен"
        );

        Allure.step("Logout admin user");
        transferPage.clickBackToDashboardButton();
        dashboardPage.openSettings();
        settingsPage.clickLogoutButton();
        assertTrue(loginPage.isLoginButtonVisible(), "Login button should be visible after logout");

        Allure.step("Login as lisa and verify updated balance");
        loginPage.loginAsUserAndWaitForDashboard("lisa", "testing123");
        String expectedBalance = "100.0";
        String actualBalance = dashboardPage.getBalanceAmountText();
        assertEquals(expectedBalance, actualBalance,
                "Lisa should have the expected balance after transfer");
    }

    @DisplayName("User lisa can log into the app")
    @Description("Verifies login for user lisa and key dashboard content visibility.")
    @Story("Login")
    @Test
    public void loginAsLisaAndOpenDashboard() {
        loginPage.loginAsUserAndWaitForDashboard("lisa", "testing123");
        assertTrue(dashboardPage.isTransactionsButtonVisible(), "Transactions navigation button is missing on the dashboard");
    }

    @DisplayName("User john can log in and see $500 balance")
    @Test
    public void loginAsJohnAndCheckBalance() {
        loginPage.loginAsUserAndWaitForDashboard("john", "testing123");
        assertTrue(dashboardPage.getBalanceAmountText().contains("500"), "John should see balance 500");
    }

    @DisplayName("App data can be cleared")
    @Test
    public void openSettingsAndChangeLanguage(){
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();
        settingsPage.clickClearAllDataButton();
        assertTrue(loginPage.isLoginButtonVisible(), "Login button should be visible after clearing app data");
    }

    @DisplayName("Admin makes 2 transfers to nick (500 and 50), and nick sees entries in Recent Transactions")
    @Description("End-to-end scenario: admin sends nick 500 and 50, then nick verifies both entries in transaction history.")
    @Story("Transactions")
    @Test
    public void adminTransfers500And50ToNickAndNickChecksTransactions() {
        loginPage.loginAsAdminAndWaitForDashboard();

        dashboardPage.clickTransferButton();
        transferPage.enterRecipient("nick");
        transferPage.enterAmount("500");
        transferPage.clickSubmitTransferButton();
        assertTextEqualsAny(transferPage.getTransferSuccessToastText(), "Transfer completed", "Перевод выполнен");
        transferPage.clickBackToDashboardButton();

        dashboardPage.clickTransferButton();
        transferPage.enterRecipient("nick");
        transferPage.enterAmount("50");
        transferPage.clickSubmitTransferButton();
        assertTextEqualsAny(transferPage.getTransferSuccessToastText(), "Transfer completed", "Перевод выполнен");
        transferPage.clickBackToDashboardButton();

        dashboardPage.openSettings();
        settingsPage.clickLogoutButton();
        loginPage.loginAsUserAndWaitForDashboard("nick", "nick123");

        dashboardPage.openTransactions();
        assertTrue(transactionsPage.isTransactionsListVisible(), "Transactions list should be visible");

        String today = LocalDate.now().toString();
        assertEquals("Transfer from admin to nick", transactionsPage.getTransactionTitleByIndex(0));
        assertEquals("-$50.00", transactionsPage.getTransactionAmountByIndex(0));
        assertEquals(today, transactionsPage.getTransactionDateByIndex(0));
        assertEquals("Transfer from admin to nick", transactionsPage.getTransactionTitleByIndex(1));
        assertEquals("-$500.00", transactionsPage.getTransactionAmountByIndex(1));
        assertEquals(today, transactionsPage.getTransactionDateByIndex(1));
    }
}
