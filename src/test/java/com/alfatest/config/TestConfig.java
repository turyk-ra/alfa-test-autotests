package com.alfatest.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

// значения из -Dkey=value перекрывают config.properties
public final class TestConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private TestConfig() {
    }

    public static URL appiumUrl() {
        String url = get("appium.url");
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new IllegalStateException("Некорректный appium.url: " + url, e);
        }
    }

    public static String appPath() {
        Path path = Path.of(get("app.path")).toAbsolutePath().normalize();
        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException("APK не найден: " + path
                    + ". Соберите приложение (./gradlew assembleDebug в qa-mobile) или укажите -Dapp.path");
        }
        return path.toString();
    }

    public static String appPackage() {
        return get("app.package");
    }

    public static String appActivity() {
        return get("app.activity");
    }

    public static Optional<String> deviceUdid() {
        return getOptional("device.udid");
    }

    public static Duration explicitTimeout() {
        return seconds("timeout.explicit");
    }

    public static Duration loginTimeout() {
        return seconds("timeout.login");
    }

    public static Duration newCommandTimeout() {
        return seconds("appium.newCommandTimeout");
    }

    public static String validLogin() {
        return get("user.login");
    }

    public static String validPassword() {
        return get("user.password");
    }

    private static Duration seconds(String key) {
        String value = get(key);
        try {
            return Duration.ofSeconds(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Параметр " + key + " должен быть числом секунд, получено: " + value, e);
        }
    }

    private static String get(String key) {
        return getOptional(key)
                .orElseThrow(() -> new IllegalStateException("Не задан параметр " + key + " в " + CONFIG_FILE));
    }

    private static Optional<String> getOptional(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        return value == null || value.isBlank() ? Optional.empty() : Optional.of(value.trim());
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(CONFIG_FILE + " не найден в src/test/resources");
            }
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать " + CONFIG_FILE, e);
        }
        return properties;
    }
}
