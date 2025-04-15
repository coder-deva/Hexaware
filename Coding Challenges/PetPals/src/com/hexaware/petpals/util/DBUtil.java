package com.hexaware.petpals.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/petpals?useSSL=false";
        String user = "root";
        String password = "deva1234";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database", e);
        }
    }
}


