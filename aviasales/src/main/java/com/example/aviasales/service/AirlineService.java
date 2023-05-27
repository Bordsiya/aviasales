package com.example.aviasales.service;

import java.util.HashSet;
import java.util.Set;

import bitronix.tm.BitronixTransactionManager;
import com.example.aviasales.dto.AirlineDTO;
import com.example.aviasales.dto.requests.AddAirlinesDTO;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.exception.TransactionException;
import com.example.aviasales.exception.not_found.AirlineNotFoundException;
import com.example.aviasales.repo.AirlineRepository;
import com.example.aviasales.util.mappers.AirlineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirlineService {
    Logger log = LoggerFactory.getLogger(AirlineService.class);
    private AirlineRepository airlineRepository;
    private AirlineMapper airlineMapper;
    private BitronixTransactionManager bitronixTransactionManager;

    @Autowired
    public AirlineService(AirlineRepository airlineRepository,
                          AirlineMapper airlineMapper,
                          BitronixTransactionManager bitronixTransactionManager) {
        this.airlineRepository = airlineRepository;
        this.airlineMapper = airlineMapper;
        this.bitronixTransactionManager = bitronixTransactionManager;
    }
    public Airline getAirlineById(Long airlineId) {
        return airlineRepository.findById(airlineId).orElseThrow(() -> new AirlineNotFoundException(airlineId));
    }
    public Set<Tariff> getAirlineTariffsByAirlineId(Long airlineId) {
        Airline airline = getAirlineById(airlineId);
        return airline.getTariffs();
    }

    public Set<Airline> addAirlines(AddAirlinesDTO addAirlinesDTO) {
        try {
            bitronixTransactionManager.begin();
            Set<Airline> airlines = new HashSet<>();
            for (AirlineDTO airlineDTO : addAirlinesDTO.getAirlines()) {
                airlines.add(airlineRepository.save(airlineMapper.fromDTO(airlineDTO, new HashSet<>(), new HashSet<>())));
            }
            bitronixTransactionManager.commit();
            return airlines;
        }
        catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("adding airlines - " + e.getMessage());
        }

    }
}
