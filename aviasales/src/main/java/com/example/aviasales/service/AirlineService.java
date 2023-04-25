package com.example.aviasales.service;

import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.exception.not_found.AirlineNotFoundException;
import com.example.aviasales.repo.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AirlineService {
    private AirlineRepository airlineRepository;
    @Autowired
    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }
    public Airline getAirlineById(Long airlineId) {
        return airlineRepository.findById(airlineId).orElseThrow(() -> new AirlineNotFoundException(airlineId));
    }
    public Set<Tariff> getAirlineTariffsByAirlineId(Long airlineId) {
        Airline airline = getAirlineById(airlineId);
        return airline.getTariffs();
    }
}
