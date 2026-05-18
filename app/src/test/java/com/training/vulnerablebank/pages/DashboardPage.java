package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class DashboardPage extends BasePage {

    private final By makeTransferButton = AppiumBy.accessibilityId("make_transfer_button");

    private final By viewTransactionsButton = AppiumBy.accessibilityId("view_transactions_button");
    private final By settingsButton = AppiumBy.accessibilityId("settings_button");

    private final By balanceAmountText = AppiumBy.accessibilityId("balance_amount_text");

    public DashboardPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void clickTransferButton() {
        click(makeTransferButton);
    }

    public boolean isTransferButtonVisible() {
        return isElementVisible(makeTransferButton, 5);
    }

    public void openTransactions(){
        click(viewTransactionsButton);
    }

    public boolean isTransactionsButtonVisible() {
        return isElementVisible(viewTransactionsButton, 5);
    }

    public void openSettings(){
        click(settingsButton);
    }

    public boolean isSettingsButtonVisible() {
        return isElementVisible(settingsButton, 5);
    }


    public String getBalanceAmountText() {
        return findVisible(balanceAmountText).getText();
    }

    public void assertPrimaryActionsVisible() {
        findVisible(makeTransferButton);
        findVisible(viewTransactionsButton);
        findVisible(settingsButton);
    }

}
