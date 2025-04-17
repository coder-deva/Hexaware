package com.hexaware.carconnect.service;

import com.hexaware.carconnect.dao.IAdminService;
import com.hexaware.carconnect.report.ReportGenerator;

import com.hexaware.carconnect.entity.Admin;
import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.entity.Reservation;
import com.hexaware.carconnect.exception.AdminNotFoundException;
import com.hexaware.carconnect.exception.DatabaseConnectionException;
import com.hexaware.carconnect.util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public   class AdminService implements IAdminService {

    private Connection connection;

    public AdminService() throws DatabaseConnectionException {
        try {
            this.connection = DBConnUtil.getConnection();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database.", e);
        }
    }

    @Override
    public Admin getAdminById(int adminId) throws AdminNotFoundException {
        String query = "SELECT * FROM Admin WHERE AdminID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, adminId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            } else {
                throw new AdminNotFoundException("Admin with ID " + adminId + " not found.");
            }
        } catch (SQLException e) {
            throw new AdminNotFoundException("Error while fetching admin by ID.", e);
        }
    }

    @Override
    public Admin getAdminByUsername(String username) throws AdminNotFoundException {
        String query = "SELECT * FROM Admin WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            } else {
                throw new AdminNotFoundException("Admin with username '" + username + "' not found.");
            }
        } catch (SQLException e) {
            throw new AdminNotFoundException("Error while fetching admin by username.", e);
        }
    }

    @Override
    public boolean addAdmin(Admin admin) {
        String query = "INSERT INTO Admin (FirstName, LastName, Email, PhoneNumber, Username, Password, Role, JoinDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, admin.getFirstName());
            stmt.setString(2, admin.getLastName());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPhoneNumber());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getPassword());
            stmt.setString(7, admin.getRole());
            stmt.setString(8, admin.getJoinDate());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding admin", e);
        }
    }
    
    @Override
    public boolean updateAdmin(Admin admin) {
        String query = "UPDATE Admin SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Username = ?, Password = ?, Role = ?, JoinDate = ? WHERE AdminID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, admin.getFirstName());
            stmt.setString(2, admin.getLastName());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPhoneNumber());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getPassword());
            stmt.setString(7, admin.getRole());
            stmt.setString(8, admin.getJoinDate());
            stmt.setInt(9, admin.getAdminID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating admin", e);
        }
    }


    @Override
    public boolean deleteAdmin(int adminId) {
        String query = "DELETE FROM Admin WHERE AdminID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, adminId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting admin", e);
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
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
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching customers", e);
        }
        return customers;
    }


    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation(
                    rs.getInt("ReservationID"),
                    rs.getInt("CustomerID"),
                    rs.getInt("VehicleID"),
                    rs.getDate("StartDate").toLocalDate(),
                    rs.getDate("EndDate").toLocalDate(),
                    rs.getDouble("TotalCost"),
                    rs.getString("Status")
                );

                
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
                if (daysBetween > 0) {
                    double dailyRate = reservation.getTotalCost() / daysBetween;
                    reservation.setDailyRate(dailyRate); 
                } else {
                    reservation.setDailyRate(0.0); 
                }
                
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reservations", e);
        }
        return reservations;
    }
    
    @Override
    public void generateReservationHistoryReport() throws DatabaseConnectionException {
        List<Reservation> reservations = getAllReservations();
        ReportGenerator.generateReservationHistoryReport(reservations);
    }

    @Override
    public void generateRevenueReport() throws DatabaseConnectionException {
        List<Reservation> reservations = getAllReservations();
        ReportGenerator.generateRevenueReport(reservations);
    }

    @Override
    public void generateVehicleUtilizationReport() throws DatabaseConnectionException {
        List<Reservation> reservations = getAllReservations();
        ReportGenerator.generateVehicleUtilizationReport(reservations);
    }


    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
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
    }
}
