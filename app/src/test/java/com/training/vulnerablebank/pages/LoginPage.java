package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class LoginPage extends BasePage {

    private final By usernameField = AppiumBy.accessibilityId("username_field");
    private final By passwordField = AppiumBy.accessibilityId("password_field");
    private final By loginButton = AppiumBy.accessibilityId("login_button");
    private final By dashboardButton = AppiumBy.accessibilityId("view_transactions_button");

    public LoginPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void loginAsAdminAndWaitForDashboard() {
        loginAsUserAndWaitForDashboard("admin", "password123");
    }

    public void loginAsUserAndWaitForDashboard(String username, String password) {
        if (isElementVisible(dashboardButton, 2)) {
            return;
        }

        if (isElementVisible(loginButton, 2)) {
            performLogin(username, password);
            findVisible(dashboardButton);
            return;
        }

        driver.navigate().back();

        if (isElementVisible(dashboardButton, 2)) {
            return;
        }

        if (isElementVisible(loginButton, 2)) {
            performLogin(username, password);
            findVisible(dashboardButton);
            return;
        }

        throw new IllegalStateException("Не удалось попасть на Login или Dashboard screen");
    }

    private void performLogin(String username, String password) {
        enterText(usernameField, username);
        enterText(passwordField, password);
        click(loginButton);
    }

    public boolean isLoginButtonVisible(){
        return isElementVisible(loginButton, 5);
    }
}
