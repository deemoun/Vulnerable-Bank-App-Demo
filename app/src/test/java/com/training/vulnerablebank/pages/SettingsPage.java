package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class SettingsPage extends BasePage {
    private final By screenHeading = AppiumBy.accessibilityId("settings_heading");
    private final By backToDashboardButton = AppiumBy.accessibilityId("Back to Dashboard");
    private final By clearAllDataButton = AppiumBy.accessibilityId("clear_all_data_button");
    private final By networkConnectionTestButton = AppiumBy.accessibilityId("network_connection_test_button");
    private final By logoutButton = AppiumBy.accessibilityId("logout_button");
    private final By russianLanguageButton = AppiumBy.accessibilityId("language_russian_button");
    private final By englishLanguageButton = AppiumBy.accessibilityId("language_english_button");

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

    public String waitForNetworkTestToastText() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement toast = shortWait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.Toast[1]"))
        );

        String text = toast.getAttribute("text");
        if (text == null || text.isBlank()) {
            text = toast.getText();
        }
        if (text == null || text.isBlank()) {
            text = toast.getAttribute("name");
        }
        return text == null ? "" : text;
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
