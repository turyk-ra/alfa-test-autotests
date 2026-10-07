package com.alfatest.utils;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public final class AllureAttachments {

    private static final Logger LOG = LoggerFactory.getLogger(AllureAttachments.class);

    private AllureAttachments() {
    }

    public static void attachScreenState(AndroidDriver driver) {
        try {
            byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Скриншот", "image/png", new ByteArrayInputStream(screenshot), "png");
        } catch (WebDriverException e) {
            LOG.warn("Не удалось снять скриншот: {}", e.getMessage());
        }
        try {
            byte[] source = driver.getPageSource().getBytes(StandardCharsets.UTF_8);
            Allure.addAttachment("Дерево элементов", "text/xml", new ByteArrayInputStream(source), "xml");
        } catch (WebDriverException e) {
            LOG.warn("Не удалось получить дерево элементов: {}", e.getMessage());
        }
    }
}
