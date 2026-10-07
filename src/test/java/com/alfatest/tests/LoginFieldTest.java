package com.alfatest.tests;

import com.alfatest.data.ExpectedTexts;
import com.alfatest.data.TestData;
import com.alfatest.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Авторизация")
@Feature("Поле логина")
public class LoginFieldTest extends BaseTest {

    @Test(description = "TC-20 Граница длины логина: попытка ввести 51-й символ")
    @Severity(SeverityLevel.NORMAL)
    @Description("Логин не длиннее 50 символов, лишнее обрезается. Падает на D-5")
    public void loginMaxLength() {
        String tooLongLogin = TestData.latinLetters(ExpectedTexts.MAX_FIELD_LENGTH + 1);
        LoginPage loginPage = new LoginPage().enterLogin(tooLongLogin);

        assertThat(loginPage.getLoginValue())
                .as("В поле логина не больше %d символов (D-5)", ExpectedTexts.MAX_FIELD_LENGTH)
                .matches("^[A-Za-z]{" + ExpectedTexts.MAX_FIELD_LENGTH + "}$")
                .isEqualTo(tooLongLogin.substring(0, ExpectedTexts.MAX_FIELD_LENGTH));
    }
}
