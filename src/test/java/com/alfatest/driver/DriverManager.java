package com.alfatest.driver;

import io.appium.java_client.android.AndroidDriver;

public final class DriverManager {

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void startDriver() {
        if (DRIVER.get() != null) {
            throw new IllegalStateException("Драйвер уже запущен в этом потоке");
        }
        DRIVER.set(DriverFactory.createAndroidDriver());
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Драйвер не запущен: страница создана вне теста или до @BeforeMethod");
        }
        return driver;
    }

    public static boolean isStarted() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } finally {
            DRIVER.remove();
        }
    }
}
