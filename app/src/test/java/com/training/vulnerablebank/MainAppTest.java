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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("VulnerableBank Mobile App")
@Feature("Core user flows")
public class MainAppTest extends TestBase {

    @DisplayName("Пользователь может войти в приложение")
    @Description("Проверка логина admin и перехода в раздел транзакций из дашборда.")
    @Story("Login")
    @Owner("qa")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginAndOpenTransactions() {
        Allure.step("Login as admin user");
        loginPage.loginAsAdminAndWaitForDashboard();
        Allure.step("Open transactions and validate heading");
        assertTrue(dashboardPage.isTransactionsButtonVisible(), "Отсутствует кнопка перехода к транзакциям на дашборде");
        dashboardPage.openTransactions();
        assertTrue(transactionsPage.isBackToDashboardButtonVisible(), "Отсутствует кнопка возврата на дашборд на экране транзакций");
        assertTextEqualsAny(
                transactionsPage.getScreenHeadingText(),
                "Recent Transactions",
                "Последние транзакции"
        );
    }

    @DisplayName("Пользователь может сделать перевод пользователю lisa")
    @Description("Проверка перевода с дашборда: admin отправляет сумму пользователю lisa и получает подтверждение.")
    @Story("Transfer")
    @Owner("qa")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginAndTransferToLisa() {
        Allure.step("Login and open transfer screen");
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Отсутствует кнопка перевода на дашборде");

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
    @Description("Проверка переводов с разными суммами и валидацией успешного завершения операции.")
    @Story("Transfer")
    public void loginAndMakeTransferWithDifferentAmounts(String amount) {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Отсутствует кнопка перевода на дашборде");

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
    @Description("Базовая проверка перевода после логина admin.")
    @Story("Transfer")
    @Test
    public void loginAndMakeTransfer() {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Отсутствует кнопка перевода на дашборде");
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

    @DisplayName("Пользователь не может сделать перевод несуществующему получателю")
    @Description("Негативная проверка: перевод на несуществующего адресата должен завершиться ошибкой валидации.")
    @Story("Transfer")
    @Test
    public void loginAndFailTransferToUnknownRecipient() {
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Отсутствует кнопка перевода на дашборде");

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

    @DisplayName("Пользователь может переключить язык на русский")
    @Description("Проверка изменения языка интерфейса в настройках на русский.")
    @Story("Settings")
    @Test
    public void switchLanguageToRussianFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isRussianLanguageButtonVisible(), "Отсутствует кнопка переключения на русский язык");
        settingsPage.clickRussianLanguageButton();

        assertEquals("Настройки", settingsPage.getScreenHeadingText(), "Заголовок экрана должен быть «Настройки» после переключения на русский");
    }

    @DisplayName("Пользователь может открыть настройки")
    @Description("Проверка доступности основных действий на экране настроек.")
    @Story("Settings")
    @Test
    public void loginAndOpenSettings(){
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isSettingsButtonVisible(), "Отсутствует кнопка настроек на дашборде");
        dashboardPage.openSettings();
        assertTrue(settingsPage.isBackToDashboardButtonVisible(), "Отсутствует кнопка «Назад» (возврат на дашборд) в настройках");
        assertTrue(settingsPage.isClearAllDataButtonVisible(), "Отсутствует кнопка очистки данных в настройках");
        assertTrue(settingsPage.isNetworkConnectionTestButtonVisible(), "Отсутствует кнопка проверки сети в настройках");
        assertTrue(settingsPage.isLogoutButtonVisible(), "Отсутствует кнопка выхода из аккаунта в настройках");
        assertTextEqualsAny(
                settingsPage.getScreenHeadingText(),
                "Settings",
                "Настройки"
        );
    }


    @DisplayName("Пользователь может вернуться в дашборд из настроек")
    @Description("Проверка возврата на дашборд через кнопку назад в настройках.")
    @Story("Dashboard")
    @Test
    public void openSettingsAndReturnToDashboard() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isBackToDashboardButtonVisible(), "Отсутствует кнопка «Назад» (возврат на дашборд) в настройках");
        settingsPage.clickBackToDashboardButton();

        assertTrue(dashboardPage.isTransferButtonVisible(), "Отсутствует кнопка перевода на дашборде");
    }

    @DisplayName("Пользователь может переключить язык обратно на английский")
    @Description("Проверка переключения языка в обе стороны: русский -> английский.")
    @Story("Settings")
    @Test
    public void switchLanguageBackToEnglishFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isRussianLanguageButtonVisible(), "Отсутствует кнопка переключения на русский язык");
        settingsPage.clickRussianLanguageButton();
        assertEquals("Настройки", settingsPage.getScreenHeadingText(), "Заголовок экрана должен быть «Настройки» после переключения на русский");

        assertTrue(settingsPage.isEnglishLanguageButtonVisible(), "Отсутствует кнопка переключения на английский язык");
        settingsPage.clickEnglishLanguageButton();
        assertEquals("Settings", settingsPage.getScreenHeadingText(), "Заголовок экрана должен быть Settings после переключения на английский");
    }

    @DisplayName("Пользователь может запустить сетевую проверку из настроек")
    @Description("Проверка запуска встроенной сетевой диагностики и получения результата.")
    @Story("Settings")
    @Test
    public void runNetworkConnectionTestFromSettings() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();

        assertTrue(settingsPage.isNetworkConnectionTestButtonVisible(), "Отсутствует кнопка проверки сети в настройках");
        settingsPage.clickNetworkConnectionTestButton();

        String toastText = settingsPage.waitForNetworkTestToastText();
        assertTrue(toastText.contains("Network test successful")
                || toastText.contains("Сетевая проверка успешна"),
                "Тост с результатом сетевой проверки не появился или содержит неожиданный текст: " + toastText);
    }


    @DisplayName("Пользователь может выйти и снова войти")
    @Description("Проверка сценария logout/login для текущего пользователя.")
    @Story("Login")
    @Test
    public void logoutAndLoginAgain() {
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();
        settingsPage.clickLogoutButton();

        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.isTransferButtonVisible(), "Отсутствует кнопка перевода на дашборде");
    }


    @DisplayName("Admin переводит 100$ пользователю lisa, и баланс lisa увеличивается на 100$")
    @Description("Сквозной сценарий: перевод от admin пользователю lisa и проверка изменения баланса после перелогина.")
    @Story("Dashboard")
    @Test
    public void adminTransfer100ToLisaAndCheckLisaBalance() {
        Allure.step("Login as admin and verify initial balance");
        loginPage.loginAsAdminAndWaitForDashboard();
        assertTrue(dashboardPage.getBalanceAmountText().contains("1,000")
                        || dashboardPage.getBalanceAmountText().contains("1000"),
                "У admin перед переводом должен отображаться баланс 1000");

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
        assertTrue(loginPage.isLoginButtonVisible(), "После выхода должна быть видна кнопка входа");

        Allure.step("Login as lisa and verify updated balance");
        loginPage.loginAsUserAndWaitForDashboard("lisa", "testing123");
        String expectedBalance = "100.0";
        String actualBalance = dashboardPage.getBalanceAmountText();
        assertEquals(expectedBalance, actualBalance,
                "После перевода у lisa должен быть ожидаемый баланс");
    }

    @DisplayName("Пользователь lisa может войти в приложение")
    @Description("Проверка логина пользователя lisa и отображения ключевого контента дашборда.")
    @Story("Login")
    @Test
    public void loginAsLisaAndOpenDashboard() {
        loginPage.loginAsUserAndWaitForDashboard("lisa", "testing123");
        assertTrue(dashboardPage.isTransactionsButtonVisible(), "Отсутствует кнопка перехода к транзакциям на дашборде");
    }

    @DisplayName("Пользователь john может войти и видеть баланс 500$")
    @Test
    public void loginAsJohnAndCheckBalance() {
        loginPage.loginAsUserAndWaitForDashboard("john", "testing123");
        assertTrue(dashboardPage.getBalanceAmountText().contains("500"), "У john должен отображаться баланс 500");
    }

    @DisplayName("Можно очистить данные приложения")
    @Test
    public void openSettingsAndChangeLanguage(){
        loginPage.loginAsAdminAndWaitForDashboard();
        dashboardPage.openSettings();
        settingsPage.clickClearAllDataButton();
        assertTrue(loginPage.isLoginButtonVisible(), "После очистки данных должна быть видна кнопка входа");
    }
}
