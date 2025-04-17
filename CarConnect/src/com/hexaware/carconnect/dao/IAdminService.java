package com.hexaware.carconnect.dao;

import com.hexaware.carconnect.entity.Admin;

import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.entity.Reservation;

import com.hexaware.carconnect.exception.AdminNotFoundException;

import java.util.List;

public interface IAdminService {
    Admin getAdminById(int adminId) throws AdminNotFoundException;
    Admin getAdminByUsername(String username) throws AdminNotFoundException;
    boolean addAdmin(Admin admin);
    boolean deleteAdmin(int adminId);
    boolean updateAdmin(Admin admin);

    List<Customer> getAllCustomers();
    List<Reservation> getAllReservations();

    
    void generateReservationHistoryReport() throws Exception;
    void generateRevenueReport() throws Exception;
    void generateVehicleUtilizationReport() throws Exception;

   
}

