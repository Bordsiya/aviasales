package com.example.aviasales.service;

import com.example.aviasales.dto.AircraftDTO;
import com.example.aviasales.dto.requests.AddAircraftsDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.exception.not_found.AircraftNotFoundException;
import com.example.aviasales.repo.AircraftRepository;
import com.example.aviasales.util.mappers.AircraftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AircraftService {
    private AircraftRepository aircraftRepository;
    private AirlineService airlineService;
    private AircraftMapper aircraftMapper;
    @Autowired
    public AircraftService(
            AircraftRepository aircraftRepository,
            @Lazy AirlineService airlineService,
            AircraftMapper aircraftMapper) {
        this.aircraftRepository = aircraftRepository;
        this.airlineService = airlineService;
        this.aircraftMapper = aircraftMapper;
    }
    public Aircraft getAircraftById(Long aircraftId) {
        return aircraftRepository.findById(aircraftId).orElseThrow(() -> new AircraftNotFoundException(aircraftId));
    }
    @Transactional(rollbackFor = Throwable.class)
    public Set<Aircraft> addAircrafts(AddAircraftsDTO addAircraftsDTO) {
        Set<Aircraft> aircrafts = new HashSet<>();
        for (AircraftDTO aircraftDTO : addAircraftsDTO.getAircrafts()) {
            Airline airline = airlineService.getAirlineById(aircraftDTO.getAirlineId());
            aircrafts.add(aircraftRepository.save(aircraftMapper.fromDTO(aircraftDTO, new HashSet<>(), airline)));
        }
        return aircrafts;
    }
}
