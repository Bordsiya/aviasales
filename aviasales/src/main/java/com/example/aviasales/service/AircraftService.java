package com.example.aviasales.service;

import bitronix.tm.BitronixTransactionManager;
import com.example.aviasales.dto.AircraftDTO;
import com.example.aviasales.dto.requests.AddAircraftsDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.exception.TransactionException;
import com.example.aviasales.exception.not_found.AircraftNotFoundException;
import com.example.aviasales.exception.not_found.AirlineNotFoundException;
import com.example.aviasales.repo.AircraftRepository;
import com.example.aviasales.util.mappers.AircraftMapper;
import lombok.SneakyThrows;
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
    private BitronixTransactionManager bitronixTransactionManager;

    @Autowired
    public AircraftService(
            AircraftRepository aircraftRepository,
            @Lazy AirlineService airlineService,
            AircraftMapper aircraftMapper,
            BitronixTransactionManager bitronixTransactionManager) {
        this.aircraftRepository = aircraftRepository;
        this.airlineService = airlineService;
        this.aircraftMapper = aircraftMapper;
        this.bitronixTransactionManager = bitronixTransactionManager;
    }
    public Aircraft getAircraftById(Long aircraftId) {
        return aircraftRepository.findById(aircraftId).orElseThrow(() -> new AircraftNotFoundException(aircraftId));
    }
    @SneakyThrows
    public Set<Aircraft> addAircrafts(AddAircraftsDTO addAircraftsDTO) {
        try {
            bitronixTransactionManager.begin();
            Set<Aircraft> aircrafts = new HashSet<>();
            for (AircraftDTO aircraftDTO : addAircraftsDTO.getAircrafts()) {
                Airline airline = airlineService.getAirlineById(aircraftDTO.getAirlineId());
                aircrafts.add(aircraftRepository.save(aircraftMapper.fromDTO(aircraftDTO, new HashSet<>(), airline)));
            }
            bitronixTransactionManager.commit();
            return aircrafts;
        }
        catch (Exception e) {
            bitronixTransactionManager.rollback();
            throw new TransactionException("adding aircrafts - " + e.getMessage());
        }
    }
}
