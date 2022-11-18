package org.homeworks.hw02.analysisHW02;

import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.sql.*;
import java.util.Random;

public class InstDao implements AutoCloseable {
    private final String URL = "jdbc:postgresql://localhost:5432/hw02";
    private final String USER = "postgres";
    private final String PASSWORD = "postgres";

    private final DbSpec spec = new DbSpec();
    private final DbSchema schema = spec.addDefaultSchema();

    private final Connection connection;

    public InstDao() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean dropTables() {
        String sql = SqlUtil.readSqlFile("hw02/drop_tables.sql");
        return runRequest(sql);
    }

    public boolean createTables() {
        String sql = SqlUtil.readSqlFile("hw02/create_tables.sql");
        return runRequest(sql);
    }

    public boolean createNewUser(String name, String password) {
        DbTable dbTable = schema.addTable("users");
        String sql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("name"), name)
                .addColumn(dbTable.addColumn("password"), password)
                .toString();
        return runRequest(sql);
    }

    private boolean runRequest(String sql) {
        try {
            Statement statement = connection.createStatement();
            System.out.println(sql.replaceAll(";", ";" + System.lineSeparator()));
            return statement.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    private int getCountRows(String nameTable) throws SQLException {
        String query = String.format("%s %s;", "SELECT COUNT(1) FROM", nameTable);
        ResultSet rs = connection.createStatement().executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        return new Random().nextInt(count) + 1;
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new RuntimeException(throwables);
            }
        }
    }



}
