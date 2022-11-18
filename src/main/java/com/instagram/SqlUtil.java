package com.instagram;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SqlUtil {

    public static String readSqlFile(String filename) {
        return readSqlFile(filename, "");
    }

    public static String readSqlFile(String filename, String delimiter) {
        InputStream resource = SqlUtil.class.getClassLoader().getResourceAsStream(filename);
        return new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining(delimiter));
    }

    public static String getResultToString(ResultSet resultSet, int fromColumn, int toColumn) {
        ResultSetMetaData metaData;
        try {
            metaData = resultSet.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return IntStream.range(fromColumn, toColumn)
                .boxed()
                .map(e -> {
                    try {
                        String columnName = metaData.getColumnName(e);
                        String value = resultSet.getString(e);
                        return String.format("%s - %s\n", columnName, value);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }})
                .collect(Collectors.joining());
    }
}
