package org.homeworks.hw02;

import org.example.lesson3.WorkJDBC;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.homeworks.hw02.analysisHW02.SqlUtil.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/hw02";
        String user = "postgres";
        String password = "postgres";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
        ) {
            //statement.execute(readSqlFile("hw02/drop_tables.sql"));
            //task 1
            statement.execute(readSqlFile("hw02/create_tables.sql"));

            //task 2
            createRandomUser(connection);
            createPost(connection, getRandomRecordsInTable(statement, "USERS"));
            createComment(connection, getRandomRecordsInTable(statement, "POSTS"), getRandomRecordsInTable(statement, "USERS"));
            createLike(connection, getRandomRecordsInTable(statement, "USERS"), getRandomRecordsInTable(statement, "POSTS"), getRandomRecordsInTable(statement, "COMMENTS"));

            //task 3
            printSummaryStatistics(statement);

            //task 4
            printUserInfo(connection, 3);
        }
    }


    public static boolean createRandomUser(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO  USERS (name, pasword, created_at) VALUES (?, ?, ?)");
        ps.setString(1, getRandomName());
        ps.setString(2, getRandomPsw());
        ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        return ps.execute();
    }

    public static boolean createComment(Connection connection, int postId, int userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO COMMENTS (text, post_id, user_id, created_at) VALUES (?, ?, ?, ?)");
        ps.setString(1, String.format("%s: %s-%s", "test comment", getRandomPsw(), getRandomPsw()));
        ps.setInt(2, postId);
        ps.setInt(3, userId);
        ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        return ps.executeUpdate() == 1;

    }

    public static boolean createPost(Connection connection, int userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO POSTS (text, created_at, user_id) VALUES (?, ?, ?)");
        ps.setString(1, String.format("%s: %s-%s", "test post", getRandomPsw(), getRandomPsw()));
        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        ps.setInt(3, userId);
        return ps.executeUpdate() == 1;
    }

    public static boolean createLike(Connection connection, int userId, int postId, int commentId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO LIKES (user_id, post_id, comment_id) VALUES (?, ?, ?)");
        ps.setInt(1, userId);
        ps.setInt(2, postId);
        ps.setInt(3, commentId);
        return ps.executeUpdate() == 1;
    }

    public static int getRandomRecordsInTable(Statement statement, String nameTable) throws SQLException {
        return new Random().nextInt(getCountRecordsInTable(statement, nameTable)) + 1;
    }

    public static int getCountRecordsInTable(Statement statement, String nameTable) throws SQLException {
        String query = String.format("%s %s;", "SELECT COUNT(1) FROM", nameTable);
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    public static void printSummaryStatistics(Statement statement) throws SQLException {
        System.out.println("===Summary information:===");
        System.out.printf("%s: %s%n", "Количество пользователей", getCountRecordsInTable(statement, "USERS"));
        System.out.printf("%s: %s%n", "Количество постов", getCountRecordsInTable(statement, "POSTS"));
        System.out.printf("%s: %s%n", "Количество комментариев", getCountRecordsInTable(statement, "COMMENTS"));
        System.out.printf("%s: %s%n", "Количество лайков", getCountRecordsInTable(statement, "LIKES"));
        System.out.println();
    }

    public static void printUserInfo(Connection connection, int userId) throws SQLException {
        System.out.println("===User information:===");
        if (searchUserInfo(connection, userId)) {
            printFirstPostUser(connection, userId);
            printCountComments(connection, userId);
        }
        System.out.println();
    }

    public static boolean searchUserInfo(Connection connection, int userId) throws SQLException {
        String result = "Пользователь не найден.";
        boolean userOn = false;
        String query = "SELECT * FROM USERS WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        if (resultSet.next()){
            result = String.format("Пользователь: %s%s", resultSet.getString("name"), System.lineSeparator());
            result += String.format("Дата создания: %s", resultSet.getTimestamp("created_at"));
            userOn = true;
        }
        System.out.println(result);
        return userOn;
    }

    public static void printFirstPostUser(Connection connection, int userId) throws SQLException {
        String result = "Посты для данного пользователя не найдены";
        String query = "SELECT id, text, created_at, user_id\n" +
                "\tFROM public.posts where user_id = ? order by id limit 1;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        if (resultSet.next()){
            result = String.format("Первый пост пользователя: %s", resultSet.getString("text"));
        }
        System.out.println(result);
    }

    public static void printCountComments(Connection connection, int userId) throws SQLException {
        String query = "SELECT COUNT(1) FROM COMMENTS WHERE user_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        System.out.println(String.format("Количество комментариев для данного пользователя: %s", resultSet.getString("count")));
    }
}
