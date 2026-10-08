package com.alfatest.data;

import java.util.regex.Pattern;

public final class ExpectedTexts {

    public static final Pattern LOGIN_SUCCESS = Pattern.compile("^Вход в Alfa-Test выполнен$");

    public static final Pattern WRONG_CREDENTIALS = Pattern.compile("^Введены неверные данные$");

    // текстов валидаций в требованиях нет, взяли свои
    public static final Pattern LOGIN_REQUIRED = Pattern.compile("^Введите логин$");
    public static final Pattern PASSWORD_REQUIRED = Pattern.compile("^Введите пароль$");

    public static final int MAX_FIELD_LENGTH = 50;

    private ExpectedTexts() {
    }
}
