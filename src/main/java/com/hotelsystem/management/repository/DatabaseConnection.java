package com.hotelsystem.management.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection = null;

    private static final String URL = "jdbc:postgresql://localhost:5432/project_hotel";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public Connection connectToDB() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;
        } catch (SQLException e) {

            System.out.println(e);

            return null;
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
