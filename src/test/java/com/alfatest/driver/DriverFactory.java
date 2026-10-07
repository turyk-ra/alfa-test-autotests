package com.alfatest.driver;

import com.alfatest.config.TestConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static AndroidDriver createAndroidDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp(TestConfig.appPath())
                .setAppPackage(TestConfig.appPackage())
                .setAppActivity(TestConfig.appActivity())
                .setNoReset(false) // перед каждым тестом чистим данные приложения
                .setFullReset(false)
                .setNewCommandTimeout(TestConfig.newCommandTimeout());
        TestConfig.deviceUdid().ifPresent(options::setUdid);

        return new AndroidDriver(TestConfig.appiumUrl(), options);
    }
}
