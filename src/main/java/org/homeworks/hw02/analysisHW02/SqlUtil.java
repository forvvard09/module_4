package org.homeworks.hw02.analysisHW02;

import org.example.lesson3.WorkJDBC;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SqlUtil {

    public static String readSqlFile(String filename) {
        InputStream resource = WorkJDBC.class.getClassLoader().getResourceAsStream(filename);
        return new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining(""));
    }

    public static String getRandomName() {
        final List<String> testNamesLast = Arrays.asList("Alex", "Vlad", "John", "Anna", "Ivan", "Bob", "Oleg", "Yakov", "Arsen", "Fedor", "Nikol", "Artem");
        final List<String> testNameSurname = Arrays.asList("Alekseev", "Vladimirov", "Yogov", "Annaniev", "Ivanov", "Bobov", "Egorov", "Yakovlev", "Zaharyan", "Olgin", "Nikolaev", "Artemov");
        return String.format("%s %s", testNameSurname.get(new Random().nextInt(testNameSurname.size())), testNamesLast.get(new Random().nextInt(testNamesLast.size())));
    }

    public static String getRandomPsw() {
        return String.format("%s%s", "pas", new Random().nextInt(100000));
    }

}
