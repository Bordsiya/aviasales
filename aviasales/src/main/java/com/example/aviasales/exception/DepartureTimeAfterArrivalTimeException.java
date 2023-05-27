package com.example.aviasales.exception;

import java.time.LocalDate;

public class DepartureTimeAfterArrivalTimeException extends RuntimeException {
    public DepartureTimeAfterArrivalTimeException(LocalDate departureTime, LocalDate arrivalTime) {
        super("departure-time [" + departureTime.toString()
                + "] is after arrival-time [" + arrivalTime.toString() + "].");
    }
}
