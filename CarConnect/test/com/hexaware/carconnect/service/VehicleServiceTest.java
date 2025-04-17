package com.hexaware.carconnect.service;

import com.hexaware.carconnect.entity.Vehicle;
import com.hexaware.carconnect.service.VehicleService;
import com.hexaware.carconnect.exception.DatabaseConnectionException;
import org.junit.jupiter.api.*;
import com.hexaware.carconnect.exception.VehicleNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleServiceTest {

    private static VehicleService vehicleService;

    @BeforeAll
    public static void setup() throws DatabaseConnectionException {
        vehicleService = new VehicleService();
    }

    @Test
    public void testAddNewVehicle() {
        Vehicle v = new Vehicle();
        v.setMake("Honda");
        v.setModel("City");
        v.setYear(2022);
        v.setDailyRate(2500.0);
        v.setAvailability(false);
        boolean added = vehicleService.addVehicle(v);
        assertTrue(added, "Vehicle should be added successfully");
    }

    @Test
    public void testUpdateVehicleDetails() throws VehicleNotFoundException {
        Vehicle v = vehicleService.getVehicleById(1); // vehicleId that exists
        if (v != null) {
            v.setDailyRate(0);
            boolean updated = vehicleService.updateVehicle(v);
            assertTrue(updated, "Vehicle should be updated");
        } else {
            fail("Vehicle not found to update");
        }
    }


    @Test
    public void testGetAvailableVehicles() {
        List<Vehicle> available = vehicleService.getAvailableVehicles();
        assertNotNull(available);
        assertFalse(available.isEmpty(), "Should return list of available vehicles");
    }

    @Test
    public void testGetAllVehicles() {
        List<Vehicle> all = vehicleService.getAvailableVehicles();
        assertNotNull(all);
        assertFalse(all.isEmpty(), "Should return list of all vehicles");
    }
}