package com.hexaware.carconnect.dao;

import com.hexaware.carconnect.entity.Vehicle;
import com.hexaware.carconnect.exception.VehicleNotFoundException;

import java.util.List;

public interface IVehicleService {
    Vehicle getVehicleById(int vehicleId) throws VehicleNotFoundException;
    List<Vehicle> getAvailableVehicles();
    boolean addVehicle(Vehicle vehicle);
    boolean updateVehicle(Vehicle vehicle);
    boolean removeVehicle(int vehicleId);
}
