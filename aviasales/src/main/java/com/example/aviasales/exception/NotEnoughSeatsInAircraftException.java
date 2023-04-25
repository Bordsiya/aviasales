package com.example.aviasales.exception;

public class NotEnoughSeatsInAircraftException extends RuntimeException {
    public NotEnoughSeatsInAircraftException(Long aircraftId, Integer maxCapacity) {
        super("Aircraft with aircraft-id [" + aircraftId.toString()
                + "] doesn't have enough seats, max-amount-of-seats [" + maxCapacity.toString() + "]");
    }
}
