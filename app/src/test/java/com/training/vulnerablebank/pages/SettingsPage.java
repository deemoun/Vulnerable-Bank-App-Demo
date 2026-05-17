package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class SettingsPage extends BasePage {
    private final By screenHeading = By.xpath(
            "//*[@text='Settings' or @text='Настройки']");
    private final By backToDashboardButton = AppiumBy.accessibilityId("Back to Dashboard");
    private final By clearAllDataButton = By.xpath(
            "//*[@text='Clear All Data' or @text='Удалить все данные']");
    private final By networkConnectionTestButton = By.xpath(
            "//*[@text='Network Connection Test' or @text='Проверка сети']");
    private final By logoutButton = By.xpath(
            "//*[@text='Logout' or @text='Выход']");
    private final By russianLanguageButton = By.xpath(
            "//*[@text='Русский']");
    private final By englishLanguageButton = By.xpath(
            "//*[@text='English']");

    public SettingsPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getScreenHeadingText() {
        return findVisible(screenHeading).getText();
    }

    public boolean isBackToDashboardButtonVisible() {
        return isElementVisible(backToDashboardButton, 5);
    }

    public void clickBackToDashboardButton() {
        click(backToDashboardButton);
    }

    public String getBackToDashboardButtonContentDescription() {
        return findVisible(backToDashboardButton).getAttribute("content-desc");
    }

    public boolean isClearAllDataButtonVisible() {
        return isElementVisible(clearAllDataButton, 5);
    }

    public void clickClearAllDataButton() {
        click(clearAllDataButton);
    }

    public String getClearAllDataButtonText() {
        return findVisible(clearAllDataButton).getText();
    }

    public boolean isNetworkConnectionTestButtonVisible() {
        return isElementVisible(networkConnectionTestButton, 5);
    }

    public void clickNetworkConnectionTestButton() {
        click(networkConnectionTestButton);
    }

    public String getNetworkConnectionTestButtonText() {
        return findVisible(networkConnectionTestButton).getText();
    }

    public boolean isLogoutButtonVisible() {
        return isElementVisible(logoutButton, 5);
    }

    public void clickLogoutButton() {
        click(logoutButton);
    }

    public String getLogoutButtonText() {
        return findVisible(logoutButton).getText();
    }

    public boolean isRussianLanguageButtonVisible() {
        return isElementVisible(russianLanguageButton, 5);
    }

    public void clickRussianLanguageButton() {
        click(russianLanguageButton);
    }

    public String getRussianLanguageButtonText() {
        return findVisible(russianLanguageButton).getText();
    }

    public boolean isEnglishLanguageButtonVisible() {
        return isElementVisible(englishLanguageButton, 5);
    }

    public void clickEnglishLanguageButton() {
        click(englishLanguageButton);
    }

    public String getEnglishLanguageButtonText() {
        return findVisible(englishLanguageButton).getText();
    }
}
