package com.training.vulnerablebank;

import com.training.vulnerablebank.pages.DashboardPage;
import com.training.vulnerablebank.pages.LoginPage;
import com.training.vulnerablebank.pages.SettingsPage;
import com.training.vulnerablebank.pages.TransactionsPage;
import com.training.vulnerablebank.pages.TransferPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class TestBase {

    private static final String APP_PACKAGE = "com.training.vulnerablebank";
    private static final String APP_ACTIVITY = "com.training.vulnerablebank.LoginActivity";
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    protected DashboardPage dashboardPage;
    protected TransferPage transferPage;
    protected TransactionsPage transactionsPage;
    protected SettingsPage settingsPage;
    protected LoginPage loginPage;

    @BeforeEach
    public void setUp() throws Exception {
        if (!isAppiumAvailable()) {
            throw new IllegalStateException("Appium server is not available at http://127.0.0.1:4723. Start Appium before running tests.");
        }

        UiAutomator2Options options = createOptions();

        driver = new AndroidDriver(new URL(APPIUM_SERVER_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        dashboardPage = new DashboardPage(driver, wait);
        transferPage = new TransferPage(driver, wait);
        transactionsPage = new TransactionsPage(driver, wait);
        settingsPage = new SettingsPage(driver, wait);
        loginPage = new LoginPage(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver == null) {
            return;
        }

        try {
            driver.terminateApp(APP_PACKAGE);
        } catch (Exception ignored) {
        } finally {
            driver.quit();
        }
    }

    private static boolean isAppiumAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 4723), 1000);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static UiAutomator2Options createOptions() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(APP_ACTIVITY)
                .setNoReset(false);

        String appPath = getAppPath();

        if (appPath != null && !appPath.isBlank()) {
            options.setApp(appPath);
        }

        return options;
    }

    private static String getAppPath() {
        String appPath = System.getProperty("appium.app");

        if (appPath == null || appPath.isBlank()) {
            appPath = System.getenv("APPIUM_APP");
        }

        return appPath;
    }
}