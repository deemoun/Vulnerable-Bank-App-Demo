package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class TransactionsPage extends BasePage {

    private final By screenHeading = AppiumBy.accessibilityId("transactions_heading");
    private final By backToDashboardButton = AppiumBy.accessibilityId("Back to Dashboard");
    private final By transactionsList = AppiumBy.accessibilityId("transactions_list");

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

    public boolean isTransactionsListVisible() {
        return isElementVisible(transactionsList, 5);
    }

    public String getTransactionTitleByIndex(int index) {
        return findVisible(AppiumBy.accessibilityId("transaction_item_" + index + "_title")).getText();
    }

    public String getTransactionAmountByIndex(int index) {
        return findVisible(AppiumBy.accessibilityId("transaction_item_" + index + "_amount")).getText();
    }

    public String getTransactionDateByIndex(int index) {
        return findVisible(AppiumBy.accessibilityId("transaction_item_" + index + "_date")).getText();
    }

}
