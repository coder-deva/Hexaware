package com.hexaware.carconnect.service;

import com.hexaware.carconnect.dao.IReservationService;
import com.hexaware.carconnect.entity.Reservation;
import com.hexaware.carconnect.exception.DatabaseConnectionException;
import com.hexaware.carconnect.exception.ReservationException;
import com.hexaware.carconnect.util.DBConnUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservationService {

    private Connection connection;

    public ReservationService() throws DatabaseConnectionException {
        try {
            this.connection = DBConnUtil.getConnection();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database in ReservationService", e);
        }
    }

    @Override
    public Reservation getReservationById(int reservationId) throws ReservationException {
        String query = "SELECT * FROM Reservation WHERE ReservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractReservationFromResultSet(rs);
            } else {
                throw new ReservationException("Reservation with ID " + reservationId + " not found.");
            }
        } catch (SQLException e) {
            throw new ReservationException("Error while fetching reservation by ID.", e);
        }
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) throws ReservationException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation WHERE CustomerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs)); 
            }
            if (reservations.isEmpty()) {
                throw new ReservationException("No reservations found for customer " + customerId);
            }
        } catch (SQLException e) {
            throw new ReservationException("Error while fetching reservations for customer " + customerId, e);
        }
        return reservations;
    }



    @Override
    public boolean createReservation(Reservation reservation) {
        String query = "INSERT INTO Reservation (CustomerID, VehicleID, StartDate, EndDate, TotalCost, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservation.getCustomerID());
            stmt.setInt(2, reservation.getVehicleID());
            stmt.setDate(3, Date.valueOf(reservation.getStartDate()));
            stmt.setDate(4, Date.valueOf(reservation.getEndDate()));
            stmt.setDouble(5, reservation.calculateTotalCost(reservation.getDailyRate()));
            stmt.setString(6, reservation.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE Reservation SET StartDate = ?, EndDate = ?, TotalCost = ?, Status = ? WHERE ReservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(reservation.getStartDate()));
            stmt.setDate(2, Date.valueOf(reservation.getEndDate()));
            stmt.setDouble(3, reservation.calculateTotalCost(reservation.getDailyRate()));
            stmt.setString(4, reservation.getStatus());
            stmt.setInt(5, reservation.getReservationID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean cancelReservation(int reservationId) {
        String query = "DELETE FROM Reservation WHERE ReservationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("ReservationID"),
                rs.getInt("CustomerID"),
                rs.getInt("VehicleID"),
                rs.getDate("StartDate").toLocalDate(),
                rs.getDate("EndDate").toLocalDate(),
                rs.getDouble("TotalCost"),
                rs.getString("Status")
        );
    }


    
    public boolean makeReservation(int customerId, int vehicleId, String start, String end) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            double dailyRate = fetchDailyRate(vehicleId);

            
            Reservation reservation = new Reservation();
            reservation.setCustomerID(customerId);
            reservation.setVehicleID(vehicleId);
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setDailyRate(dailyRate);
            reservation.setStatus("CONFIRMED");

           
            double totalCost = reservation.calculateTotalCost(dailyRate);
            reservation.setTotalCost(totalCost);

          
            return createReservation(reservation);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


 
    private double fetchDailyRate(int vehicleId) throws SQLException {
        String query = "SELECT DailyRate FROM Vehicle WHERE VehicleID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("DailyRate");
            } else {
                throw new SQLException("Vehicle with ID " + vehicleId + " not found.");
            }
        }
    }
}
