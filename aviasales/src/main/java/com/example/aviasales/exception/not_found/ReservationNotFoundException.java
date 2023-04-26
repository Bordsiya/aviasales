package com.example.aviasales.exception.not_found;

public class ReservationNotFoundException extends ResourceNotFoundException {
    public ReservationNotFoundException(Long reservationId) {
        super("Reservation", "reservation-id", reservationId.toString());
    }
}
