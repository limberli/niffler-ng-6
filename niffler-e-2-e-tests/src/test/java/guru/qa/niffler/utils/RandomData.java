package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomData {
    private static final Faker faker = new Faker();

    public static String generateUsername() {
        return faker.name().username();
    }

    public static String generateCategory() {
        return faker.animal().name();
    }
}