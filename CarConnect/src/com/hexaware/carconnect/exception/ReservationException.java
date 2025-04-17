package com.hexaware.carconnect.exception;

public class ReservationException extends Exception {
    public ReservationException(String message) {
        super(message);
    }

    public ReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
