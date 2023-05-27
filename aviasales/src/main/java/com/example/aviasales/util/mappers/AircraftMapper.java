package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.AircraftDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Flight;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AircraftMapper {

    public Aircraft fromDTO(AircraftDTO aircraftDTO, Set<Flight> flights, Airline airline) {
        return new Aircraft(
                null,
                aircraftDTO.getManufacturer(),
                aircraftDTO.getModel(),
                aircraftDTO.getNumberOfSeats(),
                flights,
                airline
        );
    }
}
