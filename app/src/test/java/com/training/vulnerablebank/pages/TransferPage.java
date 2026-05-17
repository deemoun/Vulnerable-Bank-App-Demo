package com.training.vulnerablebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class TransferPage extends BasePage {

    private final By recipientField = AppiumBy.accessibilityId("recipientField");
    private final By amountField = AppiumBy.accessibilityId("amountField");
    private final By submitButton = AppiumBy.accessibilityId("submit_transfer_button");

    private final By transferStatusMessage = AppiumBy.accessibilityId("transfer_status_message");

    public TransferPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void enterLisaTextInRecipientField() {
        enterText(recipientField, "lisa");
    }

    public void enterAmountTextInAmountField() {
        enterText(amountField, "50.0");
    }

    public void enterAmount(String amount) {
        enterText(amountField, amount);
    }

    public void clickSubmitTransferButton() {
        click(submitButton);
    }

    public String getTransferSuccessToastText() {
        return findVisible(transferStatusMessage).getText();
    }
}
