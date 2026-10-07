package com.alfatest.pages;

import com.alfatest.config.TestConfig;
import com.alfatest.driver.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public abstract class BasePage {

    protected final AndroidDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    protected WebElement waitVisible(By locator) {
        return waitVisible(locator, TestConfig.explicitTimeout());
    }

    protected WebElement waitVisible(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected boolean isVisibleWithin(By locator, Duration timeout) {
        try {
            waitVisible(locator, timeout);
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    protected boolean isDisplayedNow(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    protected void tap(By locator) {
        waitVisible(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement field = waitVisible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String text(By locator) {
        return waitVisible(locator).getText();
    }

    protected String attribute(By locator, String name) {
        return waitVisible(locator).getAttribute(name);
    }

    // у пустого поля getText() возвращает hint, поэтому такое значение считаем пустым
    protected String fieldValue(By locator) {
        WebElement field = waitVisible(locator);
        String text = field.getText();
        return Objects.equals(text, field.getAttribute("hint")) ? "" : text;
    }
}
