package com.hexaware.carconnect.auth;

import com.hexaware.carconnect.entity.Admin;


import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.exception.AuthenticationException;

import com.hexaware.carconnect.util.DBConnUtil;



import java.sql.*;

public class AuthenticationService {

	
    private Connection connection;

    
    public AuthenticationService() throws AuthenticationException {
        try {
            this.connection = DBConnUtil.getConnection();
        } catch (SQLException e) {
            throw new AuthenticationException("Database connection failed", e); 
        }
    }

    
    public Customer authenticateCustomer(String username, String password) throws AuthenticationException {
        String query = "SELECT * FROM Customer WHERE Username = ? AND Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("CustomerID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Email"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Address"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getDate("RegistrationDate").toLocalDate()
                );
            } else {
                throw new AuthenticationException("Invalid customer credentials.");
            }
        } catch (SQLException e) {
            throw new AuthenticationException("Error authenticating customer.", e);
        }
    }

  
    public Admin authenticateAdmin(String username, String password) throws AuthenticationException {
        String query = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	return new Admin(
            		    rs.getInt("AdminID"),
            		    rs.getString("FirstName"),
            		    rs.getString("LastName"),
            		    rs.getString("Email"),
            		    rs.getString("PhoneNumber"),
            		    rs.getString("Username"),
            		    rs.getString("Password"),
            		    rs.getString("Role"),
            		    rs.getString("JoinDate") 
            		);

            } else {
                throw new AuthenticationException("Invalid admin credentials.");
            }
        } catch (SQLException e) {
            throw new AuthenticationException("Error authenticating admin.", e);
        }
    }
}
