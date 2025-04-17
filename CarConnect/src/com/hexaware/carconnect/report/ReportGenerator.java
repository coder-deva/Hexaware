package com.hexaware.carconnect.report;

import java.util.List;

import java.util.Map;
import java.util.HashMap;

import com.hexaware.carconnect.entity.Reservation;

public class ReportGenerator {

   
    public static void generateReservationHistoryReport(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservation history available.");
            return;
        }

        System.out.println("\n--- Reservation History Report ---");
        for (Reservation r : reservations) {
            System.out.printf(
                "Reservation ID: %d | Customer ID: %d | Vehicle ID: %d | Start Date: %s | End Date: %s | Status: %s | Total Cost: $%.2f%n",
                r.getReservationID(),
                r.getCustomerID(),
                r.getVehicleID(),
                r.getStartDate(),
                r.getEndDate(),
                r.getStatus(),
                r.getTotalCost()
            );
        }
    }

  
    public static void generateRevenueReport(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No data available for revenue report.");
            return;
        }

        double totalRevenue = 0;
        for (Reservation r : reservations) {
            if ("COMPLETED".equalsIgnoreCase(r.getStatus())) {
                totalRevenue += r.getTotalCost();
            }
        }

        System.out.println("\n--- Revenue Report ---");
        System.out.printf("Total Revenue from Completed Reservations: $%.2f%n", totalRevenue);
    }

    
    public static void generateVehicleUtilizationReport(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No data available for vehicle utilization.");
            return;
        }

        Map<Integer, Integer> vehicleUsageMap = new HashMap<>();

        for (Reservation r : reservations) {
            int vehicleId = r.getVehicleID();
            vehicleUsageMap.put(vehicleId, vehicleUsageMap.getOrDefault(vehicleId, 0) + 1);
        }

        System.out.println("\n--- Vehicle Utilization Report ---");
        for (Map.Entry<Integer, Integer> entry : vehicleUsageMap.entrySet()) {
            System.out.printf("Vehicle ID: %d was used %d time(s)%n", entry.getKey(), entry.getValue());
        }
    }
}
