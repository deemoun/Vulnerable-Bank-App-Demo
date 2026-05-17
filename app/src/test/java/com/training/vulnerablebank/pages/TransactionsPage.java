package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class TransactionsPage extends BasePage {

    private final By screenHeading = By.xpath(
            "//*[@text='Recent Transactions' or @text='Последние транзакции']");

    public TransactionsPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getScreenHeadingText() {
        return findVisible(screenHeading).getText();
    }

}
