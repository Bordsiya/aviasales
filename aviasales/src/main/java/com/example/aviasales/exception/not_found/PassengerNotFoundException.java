package com.example.aviasales.exception.not_found;


public class PassengerNotFoundException extends ResourceNotFoundException {
    public PassengerNotFoundException(Long passengerId) {
        super("Passenger", "passenger-id", passengerId.toString());
    }
}
