package com.training.vulnerablebank;

import static com.training.vulnerablebank.utils.TestAssertions.assertTextEqualsAny;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MainAppTest extends TestBase {

    @DisplayName("Пользователь может войти в приложение")
    @Test
    public void loginAndOpenTransactions() {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransactionsButtonVisible());
        dashboardPage.openTransactions();
        assertTrue(transactionsPage.isBackToDashboardButtonVisible());
        assertTextEqualsAny(
                transactionsPage.getScreenHeadingText(),
                "Recent Transactions",
                "Последние транзакции"
        );
    }

    @DisplayName("Пользователь может сделать перевод пользователю lisa")
    @Test
    public void loginAndTransferToLisa() {
        loginPage.loginAsAdminAndWaitForDashboard();
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

    @ParameterizedTest
    @ValueSource(strings = {"50.0", "100.0", "500.0"})
    public void loginAndMakeTransferWithDifferentAmounts(String amount) {
        loginPage.loginAsAdminAndWaitForDashboard();
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
        loginPage.loginAsAdminAndWaitForDashboard();
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

    @DisplayName("Пользователь может переключить язык на русский")
    @Test
    public void switchLanguageToRussianFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isRussianLanguageButtonVisible());
        settingsPage.clickRussianLanguageButton();

        assertEquals("Настройки", settingsPage.getScreenHeadingText());
    }

    @DisplayName("Пользователь может открыть настройки")
    @Test
    public void loginAndOpenSettings(){
        loginPage.loginAsAdminAndWaitForDashboard();
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


    @DisplayName("Пользователь может вернуться в дашборд из настроек")
    @Test
    public void openSettingsAndReturnToDashboard() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isBackToDashboardButtonVisible());
        settingsPage.clickBackToDashboardButton();

        assertTrue(dashboardPage.isTransferButtonVisible());
    }

    @DisplayName("Пользователь может переключить язык обратно на английский")
    @Test
    public void switchLanguageBackToEnglishFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isRussianLanguageButtonVisible());
        settingsPage.clickRussianLanguageButton();
        assertEquals("Настройки", settingsPage.getScreenHeadingText());

        assertTrue(settingsPage.isEnglishLanguageButtonVisible());
        settingsPage.clickEnglishLanguageButton();
        assertEquals("Settings", settingsPage.getScreenHeadingText());
    }

    @DisplayName("Пользователь может запустить сетевую проверку из настроек")
    @Test
    public void runNetworkConnectionTestFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isNetworkConnectionTestButtonVisible());
        settingsPage.clickNetworkConnectionTestButton();

        String toastText = settingsPage.waitForNetworkTestToastText();
        assertTrue(toastText.contains("Network test successful")
                || toastText.contains("Сетевая проверка успешна"));
    }


    @DisplayName("Пользователь может выйти и снова войти")
    @Test
    public void logoutAndLoginAgain() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();
        settingsPage.clickLogoutButton();

        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible());
    }


    @DisplayName("Admin переводит 100$ пользователю lisa, и lisa видит 100$ на счете")
    @Test
    public void adminTransfer100ToLisaAndCheckLisaBalance() {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.getBalanceAmountText().contains("1,000")
                || dashboardPage.getBalanceAmountText().contains("1000"));

        dashboardPage.clickTransferButton();
        transferPage.enterLisaTextInRecipientField();
        transferPage.enterAmount("100");
        transferPage.clickSubmitTransferButton();
        assertTextEqualsAny(
                transferPage.getTransferSuccessToastText(),
                "Transfer completed",
                "Перевод выполнен"
        );

        transferPage.clickBackToDashboardButton();
        dashboardPage.openSettings();
        settingsPage.clickLogoutButton();

        loginPage.loginAsUserAndWaitForDashboard("lisa", "testing123");
        assertTrue(dashboardPage.getBalanceAmountText().contains("100"));
    }

    @DisplayName("Пользователь lisa может войти в приложение")
    @Test
    public void loginAsLisaAndOpenDashboard() {
        loginPage.loginAsUserAndWaitForDashboard("lisa", "testing123");
        assertTrue(dashboardPage.isTransactionsButtonVisible());
    }

    @DisplayName("Пользователь john может войти и видеть баланс 500$")
    @Test
    public void loginAsJohnAndCheckBalance() {
        loginPage.loginAsUserAndWaitForDashboard("john", "testing123");
        assertTrue(dashboardPage.getBalanceAmountText().contains("500"));
    }

    @DisplayName("Можно очистить данные приложения")
    @Test
    public void openSettingsAndChangeLanguage(){
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();
        settingsPage.clickClearAllDataButton();
        assertTrue(loginPage.isLoginButtonVisible());
    }
}
