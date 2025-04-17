package com.hexaware.carconnect.service;

import com.hexaware.carconnect.dao.IVehicleService;
import com.hexaware.carconnect.entity.Vehicle;
import com.hexaware.carconnect.exception.DatabaseConnectionException;
import com.hexaware.carconnect.exception.VehicleNotFoundException;
import com.hexaware.carconnect.util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleService implements IVehicleService {

    private Connection connection;

    public VehicleService() throws DatabaseConnectionException {
        try {
            connection = DBConnUtil.getConnection();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database in VehicleService", e);
        }
    }

    @Override
    public Vehicle getVehicleById(int vehicleId) throws VehicleNotFoundException {
        String query = "SELECT * FROM Vehicle WHERE VehicleID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractVehicleFromResultSet(rs);
            } else {
                throw new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE Availability = TRUE";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                vehicles.add(extractVehicleFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO Vehicle (Model, Make, Year, Color, RegistrationNumber, Availability, DailyRate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicle.getModel());
            stmt.setString(2, vehicle.getMake());
            stmt.setInt(3, vehicle.getYear());
            stmt.setString(4, vehicle.getColor());
            stmt.setString(5, vehicle.getRegistrationNumber());
            stmt.setBoolean(6, vehicle.isAvailable());
            stmt.setDouble(7, vehicle.getDailyRate());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        String query = "UPDATE Vehicle SET Model=?, Make=?, Year=?, Color=?, RegistrationNumber=?, Availability=?, DailyRate=? WHERE VehicleID=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicle.getModel());
            stmt.setString(2, vehicle.getMake());
            stmt.setInt(3, vehicle.getYear());
            stmt.setString(4, vehicle.getColor());
            stmt.setString(5, vehicle.getRegistrationNumber());
            stmt.setBoolean(6, vehicle.isAvailable());
            stmt.setDouble(7, vehicle.getDailyRate());
            stmt.setInt(8, vehicle.getVehicleID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean updateVehicleRate(int vehicleId, double newRate) {
        String query = "UPDATE Vehicle SET DailyRate=? WHERE VehicleID=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, newRate);
            stmt.setInt(2, vehicleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean removeVehicle(int vehicleId) {
        String query = "DELETE FROM Vehicle WHERE VehicleID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Vehicle extractVehicleFromResultSet(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("VehicleID"),
                rs.getString("Model"),
                rs.getString("Make"),
                rs.getInt("Year"),
                rs.getString("Color"),
                rs.getString("RegistrationNumber"),
                rs.getBoolean("Availability"),
                rs.getDouble("DailyRate")
        );
    }
}
