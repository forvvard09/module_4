package com.instagram;



import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.io.Closeable;
import java.sql.*;

public class InstDao implements Closeable {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private final Connection connection;
    private final DbSpec spec = new DbSpec();
    private final DbSchema schema = spec.addDefaultSchema();

    public InstDao() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean dropTables() {
        String dropTablesSql = SqlUtil.readSqlFile("hw/drop_tables.sql");
        return executeSqlWithNoResultSet(dropTablesSql);
    }

    public boolean createTables() {
        String createTablesSql = SqlUtil.readSqlFile("hw/create_tables.sql");
        return executeSqlWithNoResultSet(createTablesSql);
    }

    public boolean fillData() {
        String fillTablesSql = SqlUtil.readSqlFile("hw/fill_data.sql");
        return executeSqlWithNoResultSet(fillTablesSql);
    }



    public boolean addNewUser(String user, String password) {
        DbTable dbTable = schema.addTable("\"user\"");
        String sql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("name"), user)
                .addColumn(dbTable.addColumn("password"), password)
                .toString();
        return executeSqlWithNoResultSet(sql);
    }

    public boolean addNewPost(String user, Integer userId) {
        DbTable dbTable = schema.addTable("\"post\"");
        String sql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("text"), user)
                .addColumn(dbTable.addColumn("user_id"), userId)
                .toString();
        return executeSqlWithNoResultSet(sql);
    }

    public boolean addNewComment(String text, Integer userId, Integer postId) {
        DbTable dbTable = schema.addTable("\"comment\"");
        String sql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("text"), text)
                .addColumn(dbTable.addColumn("user_id"), userId)
                .addColumn(dbTable.addColumn("post_id"), postId)
                .toString();
        return executeSqlWithNoResultSet(sql);
    }

    public boolean addLike(Integer commentId, Integer userId, Integer postId) {
        DbTable dbTable = schema.addTable("\"like\"");
        String sql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("comment_id"), commentId)
                .addColumn(dbTable.addColumn("user_id"), userId)
                .addColumn(dbTable.addColumn("post_id"), postId)
                .toString();
        return executeSqlWithNoResultSet(sql);
    }

    public String getStatistics() {
        String statSql = SqlUtil.readSqlFile("sql/statistics.sql");
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(statSql);
            resultSet.next();
            return SqlUtil.getResultToString(resultSet, 1, 5);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserInfo(int id) {
        String userSql = String.format(SqlUtil.readSqlFile("hw/userinfo.txt", "\n"), id);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(userSql);
            if (resultSet.next()) {
                return SqlUtil.getResultToString(resultSet, 1, 5);
            } else {
                return "Пользователь не найден";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean executeSqlWithNoResultSet(String sql) {
        try (Statement statement = connection.createStatement()) {
            System.out.println(sql);
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
