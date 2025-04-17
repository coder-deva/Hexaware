package com.hexaware.carconnect.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DBConnUtil {
    public static Connection getConnection() throws SQLException {
    	String url = "jdbc:mysql://localhost:3306/carconnect?useSSL=false&allowPublicKeyRetrieval=true";

        String user = "root";
        String password = "deva1234"; 
        return DriverManager.getConnection(url, user, password);
    }
}


