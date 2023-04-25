package com.example.aviasales.exception;

public class NoAdultsForFlightException extends RuntimeException {
    public NoAdultsForFlightException(Long flightId) {
        super("No adults registering for flight with flight-id [" + flightId.toString() + "]");
    }
}
