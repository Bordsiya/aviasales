package com.example.aviasales.service;

import com.example.aviasales.dto.AirlineDTO;
import com.example.aviasales.dto.requests.AddAirlinesDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.exception.not_found.AirlineNotFoundException;
import com.example.aviasales.repo.AirlineRepository;
import com.example.aviasales.util.mappers.AirlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AirlineService {
    private AirlineRepository airlineRepository;
    private AircraftService aircraftService;
    private TariffService tariffService;
    private AirlineMapper airlineMapper;
    @Autowired
    public AirlineService(AirlineRepository airlineRepository,
                          @Lazy AircraftService aircraftService,
                          @Lazy TariffService tariffService,
                          AirlineMapper airlineMapper) {
        this.airlineRepository = airlineRepository;
        this.aircraftService = aircraftService;
        this.tariffService = tariffService;
        this.airlineMapper = airlineMapper;
    }
    public Airline getAirlineById(Long airlineId) {
        return airlineRepository.findById(airlineId).orElseThrow(() -> new AirlineNotFoundException(airlineId));
    }
    public Set<Tariff> getAirlineTariffsByAirlineId(Long airlineId) {
        Airline airline = getAirlineById(airlineId);
        return airline.getTariffs();
    }
    @Transactional(rollbackFor = Throwable.class)
    public Set<Airline> addAirlines(AddAirlinesDTO addAirlinesDTO) {
        Set<Airline> airlines = new HashSet<>();
        for (AirlineDTO airlineDTO : addAirlinesDTO.getAirlines()) {
            airlines.add(airlineRepository.save(airlineMapper.fromDTO(airlineDTO, new HashSet<>(), new HashSet<>())));
        }
        return airlines;
    }
}
