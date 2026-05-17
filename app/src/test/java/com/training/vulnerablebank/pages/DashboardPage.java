package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class DashboardPage extends BasePage {

    private final By makeTransferButton = AppiumBy.accessibilityId("make_transfer_button");

    private final By viewTransactionsButton = AppiumBy.accessibilityId("view_transactions_button");
    private final By settingsButton = AppiumBy.accessibilityId("settings_button");

    public DashboardPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void clickTransferButton() {
        click(makeTransferButton);
    }

    public void openTransactions(){
        click(viewTransactionsButton);
    }

    public void openSettings(){
        click(settingsButton);
    }

}
