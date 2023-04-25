package com.example.aviasales.service;

import com.example.aviasales.dto.AddPassengersDTO;
import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.dto.ReservationDTO;
import com.example.aviasales.entity.*;
import com.example.aviasales.exception.NoAdultsForFlightException;
import com.example.aviasales.exception.not_match.AirlinesNotMatchesException;
import com.example.aviasales.exception.NotEnoughSeatsInAircraftException;
import com.example.aviasales.exception.not_match.DocumentTypeNotMachKidException;
import com.example.aviasales.repo.PassengerRepository;
import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.Utils;
import com.example.aviasales.util.mappers.PassengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PassengerService {
    private PassengerRepository passengerRepository;
    private TariffService tariffService;
    private FlightService flightService;
    private ReservationService reservationService;
    private PassengerMapper passengerMapper;
    @Autowired
    public PassengerService(
            PassengerRepository passengerRepository,
            TariffService tariffService,
            FlightService flightService,
            ReservationService reservationService,
            PassengerMapper passengerMapper
    ) {
        this.passengerRepository = passengerRepository;
        this.tariffService = tariffService;
        this.flightService = flightService;
        this.reservationService = reservationService;
        this.passengerMapper = passengerMapper;
    }

    public Set<Passenger> addPassengers(AddPassengersDTO addPassengersDTO) {
        Flight flight = flightService.getFlightById(addPassengersDTO.getFlightId());

        Aircraft aircraft = flight.getAircraft();
        Long amountOfRegisteredPassengers = passengerRepository.countByFlightId(flight.getFlightId());
        if (amountOfRegisteredPassengers + addPassengersDTO.getPassengers().size() > aircraft.getNumberOfSeats()) {
            throw new NotEnoughSeatsInAircraftException(
                    aircraft.getAircraftId(), aircraft.getNumberOfSeats()
            );
        }

        long amountOfAdults = addPassengersDTO.getPassengers().stream()
                .filter(passengerDTO -> passengerDTO.getIsKid() == Boolean.FALSE)
                .count();
        if (amountOfAdults == 0) {
            throw new NoAdultsForFlightException(flight.getFlightId());
        }

        Reservation reservation = null;
        Set<Passenger> passengers = new HashSet<>();
        for (PassengerDTO passengerDTO: addPassengersDTO.getPassengers()) {
            Tariff tariff = tariffService.getTariffById(passengerDTO.getTariffId());
            if (!tariff.getAirline().equals(flight.getAircraft().getAirline())) {
                throw new AirlinesNotMatchesException(
                        "Flight", "Tariff",
                        flight.getAircraft().getAirline().getAirlineId(),
                        tariff.getAirline().getAirlineId()
                );
            }

            if(passengerDTO.getIsKid() == Boolean.TRUE && (passengerDTO.getDocumentType().equalsIgnoreCase(DocumentType.MILITARY_RECORD.name())
                    || passengerDTO.getDocumentType().equalsIgnoreCase(DocumentType.PASSPORT.name()))
            ) {
                throw new DocumentTypeNotMachKidException(passengerDTO.getDocumentType(), passengerDTO.getIsKid());
            }

            if (reservation == null) {
                reservation = reservationService.addReservation(new ReservationDTO(
                        Utils.generateReservationCode(),
                        addPassengersDTO.getPhoneNumber(),
                        addPassengersDTO.getEmail()
                ));
            }

            passengers.add(passengerRepository.save(passengerMapper.fromDto(passengerDTO, tariff, flight, reservation)));
        }
        return passengers;
    }
}
