package com.alfatest.pages;

import com.alfatest.config.TestConfig;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class MainPage extends BasePage {

    // у текста нет id
    private static final By SUCCESS_TEXT =
            By.xpath("//android.widget.TextView[contains(@text, 'выполнен')]");

    @Step("Проверить, что открыт экран успешного входа")
    public boolean isOpened() {
        return isVisibleWithin(SUCCESS_TEXT, TestConfig.loginTimeout());
    }

    public String getSuccessText() {
        return text(SUCCESS_TEXT);
    }
}
