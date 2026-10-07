package com.alfatest.tests;

import com.alfatest.config.TestConfig;
import com.alfatest.data.DataProviders;
import com.alfatest.data.ExpectedTexts;
import com.alfatest.pages.LoginPage;
import com.alfatest.pages.MainPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Авторизация")
@Feature("Вход в приложение")
public class AuthorizationTest extends BaseTest {

    @Test(description = "TC-03, TC-06 Успешная авторизация, регистр логина не учитывается"
            , dataProvider = "validCredentials", dataProviderClass = DataProviders.class)
    @Severity(SeverityLevel.BLOCKER)
    @Description("После входа открывается экран 'Вход в Alfa-Test выполнен'. Регистр логина не учитывается, "
            + "наборы TC-06 падают на D-9")
    public void successfulLogin(String caseName, String login) {
        new LoginPage().loginAs(login, TestConfig.validPassword());

        MainPage mainPage = new MainPage();
        assertThat(mainPage.isOpened())
                .as("Открыт экран успешного входа: %s", caseName)
                .isTrue();
        assertThat(mainPage.getSuccessText())
                .as("Текст экрана успешного входа: %s", caseName)
                .matches(ExpectedTexts.LOGIN_SUCCESS);
    }

    @Test(description = "TC-04, TC-05 Авторизация с неверными данными",
            dataProvider = "wrongCredentials", dataProviderClass = DataProviders.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("При неверной паре логин/пароль остаемся на экране входа, показано 'Введены неверные данные'")
    public void loginWithWrongCredentials(String caseName, String login, String password) {
        LoginPage loginPage = new LoginPage().loginAs(login, password);
        String error = loginPage.waitForErrorText();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(loginPage.isOpened())
                    .as("Остались на экране авторизации")
                    .isTrue();
            softly.assertThat(error)
                    .as("Сообщение о неуспешной авторизации")
                    .matches(ExpectedTexts.WRONG_CREDENTIALS);
            softly.assertThat(loginPage.getLoginValue())
                    .as("Логин сохранён после ошибки")
                    .isEqualTo(login);
            softly.assertThat(loginPage.getPasswordValue())
                    .as("Пароль сохранён после ошибки")
                    .isEqualTo(password);
        });
    }

    @Test(description = "TC-07 Нажатие «Войти» при пустых полях")
    @Severity(SeverityLevel.CRITICAL)
    @Description("При пустых полях должны быть сообщения 'Введите логин' и 'Введите пароль' и не должно быть "
            + "'Введены неверные данные'. Падает на D-6")
    public void loginWithEmptyFields() {
        LoginPage loginPage = new LoginPage().tapLoginButton();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(loginPage.isOpened())
                    .as("Остались на экране авторизации")
                    .isTrue();
            softly.assertThat(loginPage.waitForMessage(ExpectedTexts.LOGIN_REQUIRED))
                    .as("Показано «Введите логин» (D-6)")
                    .isTrue();
            softly.assertThat(loginPage.waitForMessage(ExpectedTexts.PASSWORD_REQUIRED))
                    .as("Показано «Введите пароль» (D-6)")
                    .isTrue();
            softly.assertThat(loginPage.isMessageShownNow(ExpectedTexts.WRONG_CREDENTIALS))
                    .as("Не показано «Введены неверные данные» (D-6)")
                    .isFalse();
        });
    }
}
