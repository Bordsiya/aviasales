package com.example.aviasales.exception.not_found;


public class FlightNotFoundException extends ResourceNotFoundException {
    public FlightNotFoundException(Long flightId) {
        super("Flight", "flight-id", flightId.toString());
    }
}
