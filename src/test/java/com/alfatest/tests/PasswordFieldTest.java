package com.alfatest.tests;

import com.alfatest.config.TestConfig;
import com.alfatest.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Авторизация")
@Feature("Поле пароля")
public class PasswordFieldTest extends BaseTest {

    @Test(description = "TC-22, TC-23 Маскирование пароля и переключение видимости значком")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Пароль скрыт после ввода, нечетное нажатие на глаз показывает, четное скрывает. "
            + "Сам символ маски проверяется вручную")
    public void passwordMaskingToggle() {
        String password = TestConfig.validPassword();
        LoginPage loginPage = new LoginPage().enterPassword(password);

        SoftAssertions.assertSoftly(softly -> {
            checkPasswordState(softly, loginPage, true, "после ввода");

            loginPage.togglePasswordVisibility();
            checkPasswordState(softly, loginPage, false, "после 1-го нажатия");

            loginPage.togglePasswordVisibility();
            checkPasswordState(softly, loginPage, true, "после 2-го нажатия");

            loginPage.togglePasswordVisibility();
            checkPasswordState(softly, loginPage, false, "после 3-го нажатия");

            softly.assertThat(loginPage.getPasswordValue())
                    .as("Переключение видимости не меняет введённый пароль")
                    .isEqualTo(password);
        });
    }

    @Step("Проверить режим пароля {when}: скрыт = {expectedMasked}")
    private void checkPasswordState(SoftAssertions softly, LoginPage loginPage, boolean expectedMasked, String when) {
        softly.assertThat(loginPage.isPasswordMasked())
                .as("Пароль скрыт маской " + when)
                .isEqualTo(expectedMasked);
        softly.assertThat(loginPage.isPasswordToggleChecked())
                .as("Значок в состоянии показать " + when)
                .isEqualTo(!expectedMasked);
    }
}
