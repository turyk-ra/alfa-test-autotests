package com.alfatest.tests;

import com.alfatest.driver.DriverManager;
import com.alfatest.utils.AllureAttachments;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void startDriver() {
        DriverManager.startDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void stopDriver(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE && DriverManager.isStarted()) {
                AllureAttachments.attachScreenState(DriverManager.getDriver());
            }
        } finally {
            DriverManager.quitDriver();
        }
    }
}
