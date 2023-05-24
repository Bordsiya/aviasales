package com.example.aviasales.exception;

import java.time.LocalDate;

public class AircraftAlreadyInUseException extends RuntimeException {
    public AircraftAlreadyInUseException(Long aircraftId, Long flightId) {
        super("aircraft with id [" + aircraftId
                + "] already in use in another flight with id [" + flightId + "].");
    }
}
