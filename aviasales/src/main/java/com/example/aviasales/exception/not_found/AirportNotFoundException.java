package com.example.aviasales.exception.not_found;


public class AirportNotFoundException extends ResourceNotFoundException {
    public AirportNotFoundException(Long airportId) {
        super("Airport", "airport-id", airportId.toString());
    }
}
