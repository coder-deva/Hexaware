package com.hexaware.carconnect.dao;

import com.hexaware.carconnect.entity.Reservation;
import com.hexaware.carconnect.exception.ReservationException;

import java.util.List;

public interface IReservationService {
    Reservation getReservationById(int reservationId) throws ReservationException;
    List<Reservation> getReservationsByCustomer(int customerId) throws ReservationException;  
    boolean createReservation(Reservation reservation);
    boolean updateReservation(Reservation reservation);
    boolean cancelReservation(int reservationId);
}
