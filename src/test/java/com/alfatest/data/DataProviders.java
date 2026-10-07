package com.alfatest.data;

import com.alfatest.config.TestConfig;
import org.testng.annotations.DataProvider;

public final class DataProviders {

    private DataProviders() {
    }

    @DataProvider(name = "wrongCredentials")
    public static Object[][] wrongCredentials() {
        return new Object[][]{
                {"TC-04 неверный пароль", TestConfig.validLogin(), TestData.WRONG_PASSWORD},
                {"TC-05 несуществующий логин", TestData.WRONG_LOGIN, TestConfig.validPassword()},
        };
    }

    @DataProvider(name = "validCredentials")
    public static Object[][] validCredentials(){
        return new Object[][]{
                {"TC-03 логин как в требованиях","Login"},
                {"TC-06 логин строчными (D-9)","login"},
                {"TC-06 логин заглавными (D-9)","LOGIN"},
                {"TC-06 логин вперемешку (D-9)","lOgIn"},
        };
    }
}
