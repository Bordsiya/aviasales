package com.example.aviasales.exception.not_found;

public class AirlineNotFoundException extends ResourceNotFoundException{
    public AirlineNotFoundException(Long airlineId) {
        super("Airline", "airline-id", airlineId.toString());
    }
}
