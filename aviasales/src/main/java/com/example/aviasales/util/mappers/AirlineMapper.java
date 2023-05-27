package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.AirlineDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Tariff;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AirlineMapper {
    public Airline fromDTO(AirlineDTO airlineDTO, Set<Aircraft> aircrafts, Set<Tariff> tariffs) {
        return new Airline(
                null,
                airlineDTO.getAirlineName(),
                aircrafts,
                tariffs
        );
    }
}
