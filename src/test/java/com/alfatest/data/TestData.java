package com.alfatest.data;

public final class TestData {

    public static final String WRONG_PASSWORD = "WrongPassword";
    public static final String WRONG_LOGIN = "WrongLogin";

    private static final String LATIN_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private TestData() {
    }

    public static String latinLetters(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(LATIN_LETTERS.charAt(i % LATIN_LETTERS.length()));
        }
        return result.toString();
    }
}
