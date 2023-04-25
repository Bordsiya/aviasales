package com.example.aviasales.service;

import com.example.aviasales.entity.Airport;
import com.example.aviasales.exception.not_found.AirportNotFoundException;
import com.example.aviasales.repo.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportService {
    private AirportRepository airportRepository;

    @Autowired
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }
    public Airport getAirportById(Long airportId) {
        return airportRepository.findById(airportId).orElseThrow(() -> new AirportNotFoundException(airportId));
    }
}
