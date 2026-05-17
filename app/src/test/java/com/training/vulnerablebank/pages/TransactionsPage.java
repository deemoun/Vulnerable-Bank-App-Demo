package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class TransactionsPage extends BasePage {

    private final By screenHeading = AppiumBy.accessibilityId("transactions_heading");
    private final By backToDashboardButton = AppiumBy.accessibilityId("Back to Dashboard");

    public TransactionsPage(AndroidDriver driver, WebDriverWait wait) {
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

}
