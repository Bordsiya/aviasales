package com.example.aviasales.service;

import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.exception.not_found.AircraftNotFoundException;
import com.example.aviasales.repo.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AircraftService {
    private AircraftRepository aircraftRepository;
    @Autowired
    public AircraftService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }
    public Aircraft getAircraftById(Long aircraftId) {
        return aircraftRepository.findById(aircraftId).orElseThrow(() -> new AircraftNotFoundException(aircraftId));
    }
}
