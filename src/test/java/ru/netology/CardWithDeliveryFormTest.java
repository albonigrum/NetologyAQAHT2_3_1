package ru.netology;


import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.openqa.selenium.Keys;
import ru.netology.Info.CardWithDeliveryFormInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.Info.CardWithDeliveryFormInfo.*;

public class CardWithDeliveryFormTest {
    static final long TIME_TO_SUCCESS_LOAD_MILLISECONDS = 15000L;

    private CardWithDeliveryFormInfo testData;

    static void clearInputField(SelenideElement elem) {
        elem.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
    }

    void pasteTestData() {
        $("[data-test-id=city] input").sendKeys(testData.city);
        clearInputField($("[data-test-id=date] input"));
        $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
        $("[data-test-id=name] input").sendKeys(testData.name);
        $("[data-test-id=phone] input").sendKeys(testData.phone);
        if (testData.agreement)
            $("[data-test-id=agreement] input").parent().click();
    }

    void pasteTestDataWithNewCity(String newCity) {
        $("[data-test-id=city] input").sendKeys(newCity);
        clearInputField($("[data-test-id=date] input"));
        $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
        $("[data-test-id=name] input").sendKeys(testData.name);
        $("[data-test-id=phone] input").sendKeys(testData.phone);
        if (testData.agreement)
            $("[data-test-id=agreement] input").parent().click();
    }

    void pasteTestDataWithNewDate(String newDate) {
        $("[data-test-id=city] input").sendKeys(testData.city);
        clearInputField($("[data-test-id=date] input"));
        $("[data-test-id=date] input").sendKeys(newDate);
        $("[data-test-id=name] input").sendKeys(testData.name);
        $("[data-test-id=phone] input").sendKeys(testData.phone);
        if (testData.agreement)
            $("[data-test-id=agreement] input").parent().click();
    }

    void pasteTestDataWithNewName(String newName) {
        $("[data-test-id=city] input").sendKeys(testData.city);
        clearInputField($("[data-test-id=date] input"));
        $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
        $("[data-test-id=name] input").sendKeys(newName);
        $("[data-test-id=phone] input").sendKeys(testData.phone);
        if (testData.agreement)
            $("[data-test-id=agreement] input").parent().click();
    }

    void pasteTestDataWithNewPhone(String newPhone) {
        $("[data-test-id=city] input").sendKeys(testData.city);
        clearInputField($("[data-test-id=date] input"));
        $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
        $("[data-test-id=name] input").sendKeys(testData.name);
        $("[data-test-id=phone] input").sendKeys(newPhone);
        if (testData.agreement)
            $("[data-test-id=agreement] input").parent().click();
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        testData = getRandomCorrectData();
    }

    @Nested
    class HappyPathTests {
        @Test
        void shouldHappyPath() {
            $("[data-test-id=city] input").sendKeys(testData.city);
            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();
            $$("button").find(exactText("Запланировать")).click();
            SelenideElement notification =
                    $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
            notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));
        }

        @Test
        void shouldHappyPathWithDashFormatDate() {
            String dateToTest = testData.dateOfMeeting.replaceAll("\\.", "-");

            $("[data-test-id=city] input").sendKeys(testData.city);
            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(dateToTest);
            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();
            $$("button").find(exactText("Запланировать")).click();
            SelenideElement notification =
                    $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
            notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));
        }
    }

    @Nested
    class IncorrectDataInFieldsTests {
        @ParameterizedTest
        @EmptySource
        @CsvSource(value = {
                //"*/-*+.,^%$#@!~`",
                "Ялта",
                "Ivanov",
                "03404309"
        })
        void shouldSendWithIncorrectCity(String incorrectCity) {
            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);

            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            $("[data-test-id=city] input").sendKeys(incorrectCity);
            $$("button").find(exactText("Запланировать")).click();
            assertTrue($("[data-test-id=city].input_invalid").isDisplayed());
        }

        @ParameterizedTest
        @EmptySource
        @CsvSource(value = {
                "*/-*+.,^%$#@!~`",
                "Ivanov",
                "03404309"
        })
        void shouldSendWithIncorrectDateIncorrectInput(String incorrectDate) {
            $("[data-test-id=city] input").sendKeys(testData.city);
            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(incorrectDate);
            $$("button").find(exactText("Запланировать")).click();
            assertTrue($("[data-test-id=date] .input.input_invalid").isDisplayed());
        }

        @Test
        void shouldSendWithIncorrectDateIncorrectDate() {
            $("[data-test-id=city] input").sendKeys(testData.city);
            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);

            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(getDateAfterToday(0));
            $$("button").find(exactText("Запланировать")).click();
            assertTrue($("[data-test-id=date] .input.input_invalid").isDisplayed());
        }

        @ParameterizedTest
        @EmptySource
        @CsvSource(value = {
                "*/*+.,^%$#@!~`",
                "Ivanov",
                "03404309"
        })
        void shouldSendWithIncorrectName(String incorrectName) {
            $("[data-test-id=city] input").sendKeys(testData.city);
            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
            $("[data-test-id=phone] input").sendKeys(testData.phone);
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            $("[data-test-id=name] input").sendKeys(incorrectName);
            $$("button").find(exactText("Запланировать")).click();
            assertTrue($("[data-test-id=name].input_invalid").isDisplayed());
        }

        @ParameterizedTest
        @EmptySource
//        @CsvSource(value = {
//                "*/*+.,^%$#@!~`",
//                "Ivanov",
//                "03404309",
//                "-12345678901",
//                "+1234567890",
//                "+123456789011"
//        })
//        Because there is not checks for phone field (exception: empty field)
        void shouldSendWithIncorrectPhone(String incorrectPhone) {
            $("[data-test-id=city] input").sendKeys(testData.city);
            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
            $("[data-test-id=name] input").sendKeys(testData.name);
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            $("[data-test-id=phone] input").sendKeys(incorrectPhone);
            $$("button").find(exactText("Запланировать")).click();
            assertTrue($("[data-test-id=phone].input_invalid").isDisplayed());
        }

        @Test
        void shouldSendWithIncorrectAgreement() {
            $("[data-test-id=city] input").sendKeys(testData.city);
            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);

            $$("button").find(exactText("Запланировать")).click();

            assertTrue($("[data-test-id=agreement].input_invalid").isDisplayed());
        }
    }

    @Nested
    class CityAndDatePopupInputTests {
        @Test
        void shouldChooseVariantCityFromList() {
            clearInputField($("[data-test-id=date] input"));
            $("[data-test-id=date] input").sendKeys(testData.dateOfMeeting);
            $("[data-test-id=name] input").sendKeys("Иванов Иван");
            $("[data-test-id=phone] input").sendKeys("+12345678901");
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            $("[data-test-id=city] input").sendKeys(testData.city.substring(0, 2));
            $$(".menu .menu-item").find(exactText(testData.city)).click();
            $$("button").find(exactText("Запланировать")).click();
            SelenideElement notification =
                    $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
            notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));
        }

        @Test
        void shouldChooseDateAfterWeekFromTable() {
            $("[data-test-id=city] input").sendKeys(testData.city);

            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            SelenideElement calendar = $(".calendar");
            $("[data-test-id=date]").click();

            LocalDate today = LocalDate.now();
            LocalDate dateToTest = today.plusDays(7);

            if (dateToTest.getMonth() != today.getMonth()) {
                calendar.$(".calendar__arrow_direction_right[data-step=\"1\"]").click();
            }

            calendar.$$(".calendar__day").find(
                    exactText(Integer.toString(dateToTest.getDayOfMonth()))).click();

            assertEquals(Integer.toString(dateToTest.getDayOfMonth()),
                    calendar.$(".calendar__day_state_current").innerText());

            $$("button").find(exactText("Запланировать")).click();
            SelenideElement notification =
                    $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
            notification.shouldHave(text("Успешно!")).
            shouldHave(text(dateToTest.format(DateTimeFormatter.ofPattern(CardWithDeliveryFormInfo.PATTERN_OF_DATE))));
        }

        @Test
        void shouldChooseDateAfterFromTable() {
            $("[data-test-id=city] input").sendKeys(testData.city);

            $("[data-test-id=name] input").sendKeys(testData.name);
            $("[data-test-id=phone] input").sendKeys(testData.phone);
            if (testData.agreement)
                $("[data-test-id=agreement] input").parent().click();

            SelenideElement calendar = $(".calendar");
            $("[data-test-id=date]").click();

            LocalDate current = LocalDate.now();
            LocalDate dateToTest =
                    LocalDate.parse(testData.dateOfMeeting, DateTimeFormatter.ofPattern(PATTERN_OF_DATE));

            while (dateToTest.getYear() > current.getYear()) {
                calendar.$(".calendar__arrow_direction_right[data-step=\"12\"]").click();
                current = current.plusYears(1);
            }

            if (dateToTest.getMonth() != current.getMonth()) {
                if (dateToTest.getMonthValue() > current.getMonthValue())
                    while (dateToTest.getMonthValue() > current.getMonthValue()) {
                        calendar.$(".calendar__arrow_direction_right[data-step=\"1\"]").click();
                        current = current.plusMonths(1);
                    }
                else
                    while (dateToTest.getMonthValue() < current.getMonthValue()) {
                        calendar.$(".calendar__arrow_direction_left[data-step=\"-1\"]").click();
                        current = current.plusMonths(-1);
                    }
            }

            calendar.$$(".calendar__day").find(
                    exactText(Integer.toString(dateToTest.getDayOfMonth()))).click();

            assertEquals(Integer.toString(dateToTest.getDayOfMonth()),
                    calendar.$(".calendar__day_state_current").innerText());

            $$("button").find(exactText("Запланировать")).click();
            SelenideElement notification =
                    $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
            notification.
                    shouldHave(text("Успешно!")).
                    shouldHave(text(dateToTest.
                            format(DateTimeFormatter.ofPattern(CardWithDeliveryFormInfo.PATTERN_OF_DATE))));
        }
    }

    @Nested
    class ReSendFormTests {
        @Nested
        class HappyPathTests {
            @Test
            void shouldHappyPathIfRefresh() {
                pasteTestData();

                $$("button").find(exactText("Запланировать")).click();
                SelenideElement success_notification =
                        $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
                success_notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));

                refresh();

                String newDate = getRandomCorrectDateOfMeeting();
                pasteTestDataWithNewDate(newDate);

                $$("button").find(exactText("Запланировать")).click();
                SelenideElement replan_notification = $("[data-test-id=replan-notification]").
                        waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);

                replan_notification.$$("button").find(exactText("Перепланировать")).click();

                success_notification.waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS).
                        shouldHave(text("Успешно!")).shouldHave(text(newDate));
            }

            @Test
            void shouldHappyPathIfReOpen() {
                pasteTestData();

                $$("button").find(exactText("Запланировать")).click();
                SelenideElement success_notification =
                        $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
                success_notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));

                closeWindow();
                open("http://localhost:9999");

                String newDate = getRandomCorrectDateOfMeeting();
                pasteTestDataWithNewDate(newDate);

                $$("button").find(exactText("Запланировать")).click();
                SelenideElement replan_notification = $("[data-test-id=replan-notification]").
                        waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);

                replan_notification.$$("button").find(exactText("Перепланировать")).click();

                success_notification.waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS).
                        shouldHave(text("Успешно!")).shouldHave(text(newDate));
            }
        }

        @Nested
        class SadPathTests  {
            @Test
            void shouldSadPathIfNewCity() {
                pasteTestData();

                $$("button").find(exactText("Запланировать")).click();
                SelenideElement success_notification =
                        $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
                success_notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));

                refresh();

                String newCity = getRandomCorrectCity();
                pasteTestDataWithNewCity(newCity);

                $$("button").find(exactText("Запланировать")).click();

                assertTrue($("[data-test-id=replan-notification]").is(hidden));

                success_notification.waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS).
                        shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));
            }

            @Test
            void shouldSadPathIfNewName() {
                pasteTestData();

                $$("button").find(exactText("Запланировать")).click();
                SelenideElement success_notification =
                        $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
                success_notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));

                refresh();

                String newName = getRandomCorrectName();
                pasteTestDataWithNewName(newName);

                $$("button").find(exactText("Запланировать")).click();

                assertTrue($("[data-test-id=replan-notification]").is(hidden));

                success_notification.waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS).
                        shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));
            }

            @Test
            void shouldSadPathIfNewPhone() {
                pasteTestData();

                $$("button").find(exactText("Запланировать")).click();
                SelenideElement success_notification =
                        $("[data-test-id=success-notification]").waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS);
                success_notification.shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));

                refresh();

                String newPhone = getRandomCorrectPhone();
                pasteTestDataWithNewPhone(newPhone);

                $$("button").find(exactText("Запланировать")).click();

                assertTrue($("[data-test-id=replan-notification]").is(hidden));

                success_notification.waitUntil(visible, TIME_TO_SUCCESS_LOAD_MILLISECONDS).
                        shouldHave(text("Успешно!")).shouldHave(text(testData.dateOfMeeting));
            }
        }
    }
}
