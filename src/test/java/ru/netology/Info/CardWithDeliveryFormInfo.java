package ru.netology.Info;

import com.github.javafaker.Faker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class CardWithDeliveryFormInfo {
    public final String city;
    public final String dateOfMeeting;
    public final String name;
    public final String phone;
    public final boolean agreement;

    public static final int MAX_DAYS_TO_AVAILABLE_TO_ORDER_FROM_TODAY = 1000;
    public static final int MIN_DAYS_TO_AVAILABLE_TO_ORDER_FROM_TODAY = 3;
    public static final String PATTERN_OF_DATE = "dd.MM.yyyy";

    static int COUNT_CORRECT_CITIES = 83;
    static final String CORRECT_CITIES_CSV_FILEPATH = "src/test/resources/CorrectCities.csv";
    static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(PATTERN_OF_DATE);



    public static String getRandomCorrectCity() {
        String returnCity = null;
        try (BufferedReader bufReader =
                     new BufferedReader(new FileReader(CORRECT_CITIES_CSV_FILEPATH, StandardCharsets.UTF_8))) {
            int randomNumber = ThreadLocalRandom.current().nextInt(1, COUNT_CORRECT_CITIES);
            for (int i = 0; i < randomNumber; ++i) {
                returnCity = bufReader.readLine();
            }
            assert returnCity != null;
            returnCity = returnCity.replaceAll(",", "");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnCity;
    }

    public static String getRandomCorrectDateOfMeeting() {
        Faker faker = new Faker(new Locale("ru"));
        return DATE_FORMATTER.format(faker.date().
                future(
                        MAX_DAYS_TO_AVAILABLE_TO_ORDER_FROM_TODAY,
                        MIN_DAYS_TO_AVAILABLE_TO_ORDER_FROM_TODAY,
                        TimeUnit.DAYS
                )
        );
    }

    public static String getDateAfterToday(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern(PATTERN_OF_DATE));
    }

    public static String getRandomCorrectName() {
        Faker faker = new Faker(new Locale("ru"));
        //TODO: Добавить поддержку букв ё
        return faker.name().fullName().replaceAll("Ё", "Е").replaceAll("ё", "е");
    }

    public static String getRandomCorrectPhone() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.phoneNumber().phoneNumber();
    }

    public static boolean getRandomCorrectAgreement() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.bool().bool();
    }

    private CardWithDeliveryFormInfo(String city,
                                     String dateOfMeeting,
                                     String name,
                                     String phone,
                                     boolean agreement) {
        this.city = city;
        this.dateOfMeeting = dateOfMeeting;
        this.name = name;
        this.phone = phone;
        this.agreement = agreement;

        int countCities = 0;
        try (BufferedReader bufReader = new BufferedReader(new FileReader(CORRECT_CITIES_CSV_FILEPATH))) {

            while (bufReader.readLine() != null) {
                ++countCities;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        COUNT_CORRECT_CITIES = countCities;

    }

    public static CardWithDeliveryFormInfo getRandomCorrectData() {
        return new CardWithDeliveryFormInfo(
                getRandomCorrectCity(),
                getRandomCorrectDateOfMeeting(),
                getRandomCorrectName(),
                getRandomCorrectPhone(),
                true
        );
    }

}