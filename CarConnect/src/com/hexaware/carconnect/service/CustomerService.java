package com.hexaware.carconnect.service;

import com.hexaware.carconnect.dao.ICustomerService;
import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.exception.DatabaseConnectionException;
import com.hexaware.carconnect.util.DBConnUtil;

import java.sql.*;
import java.time.LocalDate;

public class CustomerService implements ICustomerService {

    private Connection connection;

    public CustomerService() throws DatabaseConnectionException {
        try {
            connection = DBConnUtil.getConnection();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database in CustomerService", e);
        }
    }


    @Override
    public Customer getCustomerById(int customerId) {
        Customer customer = null;
        String query = "SELECT * FROM Customer WHERE CustomerID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                customer = mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        Customer customer = null;
        String query = "SELECT * FROM Customer WHERE Username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                customer = mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public boolean registerCustomer(Customer customer) {
        String query = "INSERT INTO Customer (FirstName, LastName, Email, PhoneNumber, Address, Username, Password, RegistrationDate) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getUsername());
            pstmt.setString(7, customer.getPassword());
            
            LocalDate regDate = customer.getRegistrationDate() != null ? customer.getRegistrationDate() : LocalDate.now();
            pstmt.setDate(8, Date.valueOf(regDate));

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // return true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;//rregistartion is failed
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE Customer SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ?, Password = ? WHERE CustomerID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getPassword());
            pstmt.setInt(7, customer.getCustomerID());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        String query = "DELETE FROM Customer WHERE CustomerID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteReservationsByCustomerId(int customerId) {
        String query = "DELETE FROM reservation WHERE CustomerID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerID(rs.getInt("CustomerID"));
        customer.setFirstName(rs.getString("FirstName"));
        customer.setLastName(rs.getString("LastName"));
        customer.setEmail(rs.getString("Email"));
        customer.setPhoneNumber(rs.getString("PhoneNumber"));
        customer.setAddress(rs.getString("Address"));
        customer.setUsername(rs.getString("Username"));
        customer.setPassword(rs.getString("Password"));
        customer.setRegistrationDate(rs.getDate("RegistrationDate").toLocalDate());
        return customer;
    }
}
