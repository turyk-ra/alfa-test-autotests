package com.alfatest.pages;

import com.alfatest.config.TestConfig;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Pattern;

public class LoginPage extends BasePage {

    private static final String APP_ID = "com.alfabank.qapp:id/";

    private static final By TITLE = AppiumBy.id(APP_ID + "tvTitle");
    private static final By LOGIN_FIELD = AppiumBy.id(APP_ID + "etUsername");
    private static final By PASSWORD_FIELD = By.cssSelector("#etPassword");
    private static final By PASSWORD_TOGGLE = AppiumBy.id(APP_ID + "text_input_end_icon");
    private static final By LOGIN_BUTTON = By.cssSelector("#btnConfirm");
    private static final By ERROR_MESSAGE = AppiumBy.id(APP_ID + "tvError");

    @Step("Проверить, что открыт экран авторизации")
    public boolean isOpened() {
        return isVisibleWithin(TITLE, TestConfig.explicitTimeout());
    }

    public String getTitleText() {
        return text(TITLE);
    }

    @Step("Ввести логин {login}")
    public LoginPage enterLogin(String login) {
        type(LOGIN_FIELD, login);
        return this;
    }

    @Step("Ввести пароль {password}")
    public LoginPage enterPassword(String password) {
        type(PASSWORD_FIELD, password);
        return this;
    }

    @Step("Нажать кнопку входа")
    public LoginPage tapLoginButton() {
        tap(LOGIN_BUTTON);
        return this;
    }

    @Step("Авторизоваться: {login} / {password}")
    public LoginPage loginAs(String login, String password) {
        return enterLogin(login)
                .enterPassword(password)
                .tapLoginButton();
    }

    @Step("Нажать значок показа пароля")
    public LoginPage togglePasswordVisibility() {
        tap(PASSWORD_TOGGLE);
        return this;
    }

    // getText() отдает реальный пароль даже под маской, поэтому смотрим на атрибут password
    public boolean isPasswordMasked() {
        return Boolean.parseBoolean(attribute(PASSWORD_FIELD, "password"));
    }

    public boolean isPasswordToggleChecked() {
        return Boolean.parseBoolean(attribute(PASSWORD_TOGGLE, "checked"));
    }

    public String getLoginValue() {
        return fieldValue(LOGIN_FIELD);
    }

    public String getPasswordValue() {
        return fieldValue(PASSWORD_FIELD);
    }

    public String getLoginButtonText() {
        return text(LOGIN_BUTTON);
    }

    public String getLoginHint() {
        return attribute(LOGIN_FIELD, "hint");
    }

    // tvError есть на экране всегда, ждем пока в нем появится текст
    @Step("Получить текст ошибки")
    public String waitForErrorText() {
        return new WebDriverWait(driver, TestConfig.loginTimeout()).until(d -> {
            String text = d.findElement(ERROR_MESSAGE).getText();
            return text == null || text.isBlank() ? null : text;
        });
    }

    // где показывать сообщения, в требованиях не сказано, поэтому ищем по всему экрану
    @Step("Дождаться сообщения {text}")
    public boolean waitForMessage(Pattern text) {
        return isVisibleWithin(messageLocator(text), TestConfig.explicitTimeout());
    }

    public boolean isMessageShownNow(Pattern text) {
        return isDisplayedNow(messageLocator(text));
    }

    private static By messageLocator(Pattern text) {
        return AppiumBy.androidUIAutomator("new UiSelector().textMatches(\"" + text.pattern() + "\")");
    }
}
