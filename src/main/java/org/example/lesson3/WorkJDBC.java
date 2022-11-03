package org.example.lesson3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class WorkJDBC {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "terrrr";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
        ) {
            statement.execute(readSqlFile("lesson3/drop_tables.sql"));
            statement.execute(readSqlFile("lesson3/create_tables.sql"));
            int insertCount = statement.executeUpdate(readSqlFile("lesson3/fill_data.sql"));

            int countUpdatedRows = statement.executeUpdate(readSqlFile("lesson3/update_books.sql"));

            ResultSet resultSet = statement.executeQuery("select * from book;");
            printAllDataFromResultSet(resultSet);

            ResultSet tables = statement.executeQuery(readSqlFile("lesson3/get_all_tables.sql"));
            printAllDataFromResultSet(tables);

            System.out.println(tableExists(connection, "book"));
            System.out.println(tableExists(connection, "booking"));
        }
    }

    public static void printColumnNames(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%s | ", resultSet.getMetaData().getColumnName(i));
        }
        System.out.println("\n______________________");
    }

    public static void printAllDataFromResultSet(ResultSet resultSet) throws SQLException {
        printColumnNames(resultSet);
        while (resultSet.next()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount ; i++) {
                System.out.printf("%s | ", resultSet.getString(i));
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, "public", tableName, new String[]{"TABLE"});
        return tables.next();
    }

    public static String readSqlFile(String filename) {
        InputStream resource = WorkJDBC.class.getClassLoader().getResourceAsStream(filename);
        return new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining(""));
    }
}
