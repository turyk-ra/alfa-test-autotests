# Alfa-Test autotests

Автотесты на экран авторизации Android-приложения Alfa-Test ([тестовое задание](https://github.com/lunin-vadim/qa-mobile)).

Сначала разобрал требования и написал тест-кейсы, потом автоматизировал часть из них.
По ходу нашлись дефекты и вопросы к требованиям, все лежат в `docs/` в виде таблиц.

Стек: Java 17, Maven, Appium 3.8 (UiAutomator2), TestNG, AssertJ, Allure.

## Документация

- [docs/test-cases.csv](docs/test-cases.csv) - тест-кейсы (29 шт.), в колонке "Автотест" видно, что автоматизировано
- [docs/defects.csv](docs/defects.csv) - найденные дефекты
- [docs/questions.csv](docs/questions.csv) - вопросы по требованиям, ответы и мои допущения (A-N)
- [docs/requirements.csv](docs/requirements.csv) - требования с номерами, на них ссылаются кейсы
- [docs/checklist.csv](docs/checklist.csv) - что проверить руками сверх требований

Во всех кейсах предусловие одно: приложение запущено с чистыми данными, открыт экран входа.

## Тесты

| Кейс | Класс | Результат |
|---|---|---|
| TC-03 вход с `Login` / `Password` | AuthorizationTest | passed |
| TC-06 логин в другом регистре: `login`, `LOGIN`, `lOgIn` | AuthorizationTest | failed, D-9 |
| TC-04 неверный пароль | AuthorizationTest | passed |
| TC-05 несуществующий логин | AuthorizationTest | passed |
| TC-07 пустые поля | AuthorizationTest | failed, D-6 |
| TC-22, TC-23 маска пароля и кнопка показа | PasswordFieldTest | passed |
| TC-20 логин длиннее 50 символов | LoginFieldTest | failed, D-5 |

TC-03/06 и TC-04/05 сделаны через `@DataProvider`, поэтому тестов 5, а запусков 9.

Красные тесты падают на известных багах. Как и просили в задании, тест доведен до места, где
мешает баг, и после фикса должен пройти без изменений. Номер дефекта есть в сообщении проверки,
в Allure такие падения попадают в категорию "Известные дефекты".

Что просили в задании и где это:
- CSS - `LoginPage`, поле пароля и кнопка
- XPath - `MainPage`, у текста "Вход в Alfa-Test выполнен" нет id
- регулярки - в проверках (`ExpectedTexts`, `LoginFieldTest`) и в локаторе `UiSelector().textMatches` в `LoginPage`
- паттерны - Page Object, фабрика драйвера, ThreadLocal для драйвера, DataProvider

## Как запустить

Нужно: JDK 17, Maven, Android SDK с эмулятором, Node.js 20+ и Appium:

```bash
npm install -g appium@3.8.0
appium driver install uiautomator2@8.7.0
```

Проверял на эмуляторе Pixel 6, Android 13 (API 33). Проверить окружение можно через `appium driver doctor uiautomator2`.

```bash
emulator -avd <имя_эмулятора>
appium
mvn clean test
mvn allure:serve
```

Эмулятор и Appium запускать в разных вкладках. APK ставится на устройство сам.
Настройки лежат в `src/test/resources/config.properties`, любую можно переопределить, например `-Ddevice.udid=emulator-5556`.

## Про APK

`app/alfa-test.apk` собран из репозитория задания (коммит `a1d9a2b`). Как есть проект не собирается, поэтому:
- убрал лишний `-` в `LoginUseCase.kt` (это D-1)
- добавил Gradle wrapper 7.5.1, его в репозитории нет
- собирал `./gradlew assembleDebug` на JDK 17

Больше в приложении ничего не менял.

## Заметки

- Маску пароля проверяю через атрибут `password`: getText() у поля отдает настоящий пароль даже в скрытом режиме. Сам символ `•` проверяется руками.
- maven-surefire-plugin зафиксирован на 3.5.6. В 3.6.0 TestNG запускается через JUnit Platform, `testng.xml` игнорируется и в Allure появляются лишние passed-результаты.
- Каждый тест открывает свою сессию Appium. Так тесты не зависят друг от друга, но каждый старт занимает ~5 сек.
- Логин и пароль в конфиге, потому что это тестовые данные из задания. В реальном проекте передавал бы через переменные окружения.
