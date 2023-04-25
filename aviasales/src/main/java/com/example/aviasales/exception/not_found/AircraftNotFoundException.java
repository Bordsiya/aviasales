package com.example.aviasales.exception.not_found;

public class AircraftNotFoundException extends ResourceNotFoundException{
    public AircraftNotFoundException(Long aircraftId) {
        super("Aircraft", "aircraft-id", aircraftId.toString());
    }
}
