package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class LoginPage extends BasePage {

    private final By loginButton = AppiumBy.accessibilityId("login_button");
    private final By dashboardButton = AppiumBy.accessibilityId("view_transactions_button");

    public LoginPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void loginAndWaitForDashboard() {
        if (isElementVisible(dashboardButton, 2)) {
            return;
        }

        if (isElementVisible(loginButton, 2)) {
            click(loginButton);
            findVisible(dashboardButton);
            return;
        }

        driver.navigate().back();

        if (isElementVisible(dashboardButton, 2)) {
            return;
        }

        if (isElementVisible(loginButton, 2)) {
            click(loginButton);
            findVisible(dashboardButton);
            return;
        }

        throw new IllegalStateException("Не удалось попасть на Login или Dashboard screen");
    }
}