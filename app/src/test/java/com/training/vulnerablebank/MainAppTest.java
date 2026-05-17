package com.training.vulnerablebank;
import static com.training.vulnerablebank.utils.TestAssertions.assertTextEqualsAny;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class MainAppTest extends TestBase {

    @DisplayName("Пользователь может войти в приложение")
    @Test
    public void loginAndOpenTransactions() {
        loginPage.loginAndWaitForDashboard();
        assertTrue(dashboardPage.isTransactionsButtonVisible());
        dashboardPage.openTransactions();
        assertTrue(transactionsPage.isBackToDashboardButtonVisible());
        assertTextEqualsAny(
                transactionsPage.getScreenHeadingText(),
                "Recent Transactions",
                "Последние транзакции"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"50.0", "100.0", "500.0"})
    public void loginAndMakeTransferWithDifferentAmounts(String amount) {
        loginPage.loginAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible());

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

    @DisplayName("Пользователь может сделать перевод")
    @Test
    public void loginAndMakeTransfer() {
        loginPage.loginAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible());
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

    @DisplayName("Пользователь может открыть настройки")
    @Test
    public void loginAndOpenSettings(){
        loginPage.loginAndWaitForDashboard();
        assertTrue(dashboardPage.isSettingsButtonVisible());
        dashboardPage.openSettings();
        assertTrue(settingsPage.isBackToDashboardButtonVisible());
        assertTrue(settingsPage.isClearAllDataButtonVisible());
        assertTrue(settingsPage.isNetworkConnectionTestButtonVisible());
        assertTrue(settingsPage.isLogoutButtonVisible());
        assertTextEqualsAny(
                settingsPage.getScreenHeadingText(),
                "Settings",
                "Настройки"
        );
    }
}
