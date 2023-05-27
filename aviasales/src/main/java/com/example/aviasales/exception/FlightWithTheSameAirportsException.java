package com.example.aviasales.exception;

public class FlightWithTheSameAirportsException extends RuntimeException {
    public FlightWithTheSameAirportsException(Long airportId) {
        super("flight with the same airports with ids [" + airportId + "].");
    }
}
