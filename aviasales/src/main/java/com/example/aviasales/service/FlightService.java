package com.example.aviasales.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import bitronix.tm.BitronixTransactionManager;
import com.example.aviasales.controller.StompController;
import com.example.aviasales.dto.FlightDTO;
import com.example.aviasales.dto.requests.AddFlightsDTO;
import com.example.aviasales.dto.requests.DeleteFlightsRequest;
import com.example.aviasales.dto.requests.MailServiceRequest;
import com.example.aviasales.dto.responses.search_response.SearchResponseDTO;
import com.example.aviasales.dto.responses.search_response.SearchResponseTariffWithPriceDTO;
import com.example.aviasales.entity.*;
import com.example.aviasales.exception.AircraftAlreadyInUseException;
import com.example.aviasales.exception.DepartureTimeAfterArrivalTimeException;
import com.example.aviasales.exception.FlightWithTheSameAirportsException;
import com.example.aviasales.exception.TransactionException;
import com.example.aviasales.exception.not_found.FlightNotFoundException;
import com.example.aviasales.repo.FlightRepository;
import com.example.aviasales.repo.MailRequestRepository;
import com.example.aviasales.repo.UserRepository;
import com.example.aviasales.util.Utils;
import com.example.aviasales.util.enums.MailRequestStatus;
import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.mappers.FlightsMapper;
import com.example.aviasales.util.mappers.SearchResponseMapper;
import com.example.aviasales.util.mappers.models.AddFlightRequest;
import com.example.aviasales.util.mappers.models.SearchRequest;
import com.example.aviasales.util.sort.SearchResponseDTOSortUtils;
import com.example.aviasales.util.sort.SearchResponseTariffWithPriceDTOSortUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class FlightService {
    private Logger log = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;
    private final AirportService airportService;
    private final AircraftService aircraftService;
    private final ReservationService reservationService;
    private final SearchResponseMapper searchResponseMapper;
    private final FlightsMapper flightsMapper;
    private final BitronixTransactionManager bitronixTransactionManager;
    private UserRepository userRepository;
    private ObjectMapper mapper;

    private StompController stompController;

    private final MailRequestRepository mailRequestRepository;

    @Autowired
    public FlightService(
            FlightRepository flightRepository,
            @Lazy AirportService airportService,
            @Lazy AircraftService aircraftService,
            @Lazy ReservationService reservationService,
            SearchResponseMapper searchResponseMapper,
            FlightsMapper flightsMapper,
            BitronixTransactionManager bitronixTransactionManager,
            MailRequestRepository mailRequestRepository,
            StompController stompController,
            UserRepository userRepository,
            ObjectMapper objectMapper
    ) {
        this.flightRepository = flightRepository;
        this.airportService = airportService;
        this.aircraftService = aircraftService;
        this.reservationService = reservationService;
        this.searchResponseMapper = searchResponseMapper;
        this.flightsMapper = flightsMapper;
        this.bitronixTransactionManager = bitronixTransactionManager;
        this.mailRequestRepository = mailRequestRepository;
        this.stompController = stompController;
        this.mapper = objectMapper;
        this.userRepository = userRepository;
    }

    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId).orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    public List<SearchResponseDTO> getFlightsFiltered(SearchRequest searchRequest) {
        Set<Flight> filteredFlights = flightRepository.getFlightsSearched(
                searchRequest.getAirportToId(),
                searchRequest.getAirportFromId(),
                searchRequest.getDepartureTimeFrom(),
                searchRequest.getArrivalTimeFrom(),
                searchRequest.getFlightDurationTimeUntilInHH(),
                searchRequest.getDateFrom()
        );
        if (searchRequest.getDateBack() != null) {
            filteredFlights.addAll(flightRepository.getFlightsSearched(
                    searchRequest.getAirportToId(),
                    searchRequest.getAirportFromId(),
                    searchRequest.getDepartureTimeFrom(),
                    searchRequest.getArrivalTimeFrom(),
                    searchRequest.getFlightDurationTimeUntilInHH(),
                    searchRequest.getDateBack()
            ));
        }
        List<SearchResponseDTO> searchResponseDTOS = new ArrayList<>();
        for (Flight flight : filteredFlights) {
            Set<Tariff> tariffs = flight.getAircraft().getAirline().getTariffs();
            Set<SearchResponseTariffWithPriceDTO> tariffsWithPrices =
                    new TreeSet<>(SearchResponseTariffWithPriceDTOSortUtils.getComparator());
            for (Tariff tariff : tariffs) {
                long price =
                        (flight.getDefaultPriceForKids() + tariff.getPriceForKids()) * searchRequest.getAmountOfChildren()
                                + (flight.getDefaultPriceForAdults() + tariff.getPriceForAdults()) * searchRequest.getAmountOfAdults();
                if (tariff.getTariffType().name().equals(searchRequest.getTariff())
                        && tariff.getHasBaggage().equals(searchRequest.getHasBaggage())
                        && price <= searchRequest.getMaxPrice()
                ) {
                    tariffsWithPrices.add(
                            new SearchResponseTariffWithPriceDTO(
                                    tariff,
                                    price
                            )
                    );
                }
            }
            searchResponseDTOS.add(
                    searchResponseMapper.toDTO(
                            flight.getAircraft().getAirline(),
                            flight,
                            flight.getDepartureAirport(),
                            flight.getArrivalAirport(),
                            tariffsWithPrices,
                            flight.getAircraft()
                    )
            );
        }

        searchResponseDTOS.sort(SearchResponseDTOSortUtils.getComparatorBySortingAlgorithm(
                SortingAlgorithm.valueOf(searchRequest.getSortingAlgorithm())));
        return Utils.getPage(searchResponseDTOS, searchRequest.getPageNumber(), searchRequest.getPageSize());
    }

    public Set<Flight> addFlights(AddFlightsDTO addFlightsDTO) {
        try {
            bitronixTransactionManager.begin();
            Set<Flight> flights = new HashSet<>();
            for (FlightDTO flightDTO : addFlightsDTO.getFlights()) {
                AddFlightRequest addFlightRequest = flightsMapper.fromDTO(flightDTO);

                Airport departureAirport = airportService.getAirportById(addFlightRequest.getDepartureAirportId());
                Airport arrivalAirport = airportService.getAirportById(addFlightRequest.getArrivalAirportId());
                Aircraft aircraft = aircraftService.getAircraftById(addFlightRequest.getAircraftId());

                if (departureAirport.getAirportId().equals(arrivalAirport.getAirportId())) {
                    throw new FlightWithTheSameAirportsException(departureAirport.getAirportId());
                }

                if (addFlightRequest.getDepartureDate().isAfter(addFlightRequest.getArrivalDate())) {
                    throw new DepartureTimeAfterArrivalTimeException(
                            addFlightRequest.getDepartureDate(), addFlightRequest.getArrivalDate());
                }

                Long flightIdUsingAircraft =
                        flightRepository.getFlightIdWithAircraftIdBetweenDepartureDateAndArrivalDate(
                                addFlightRequest.getAircraftId(), addFlightRequest.getDepartureDate(),
                                addFlightRequest.getArrivalDate());
                if (flightIdUsingAircraft != null) {
                    throw new AircraftAlreadyInUseException(addFlightRequest.getAircraftId(), flightIdUsingAircraft);
                }

                flights.add(
                        flightRepository.save(
                                flightsMapper.fromDTO(
                                        addFlightRequest,
                                        departureAirport,
                                        arrivalAirport,
                                        aircraft,
                                        new ArrayList<>()
                                )
                        )
                );
            }
            bitronixTransactionManager.commit();
            return flights;
        } catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("adding flights - " + e.getMessage());
        }
    }

    public Set<Long> deleteFlights(DeleteFlightsRequest deleteFlightsRequest) {
        try {
            bitronixTransactionManager.begin();
            Set<Long> reservationsIds = new HashSet<>();
            Set<Long> deleteFlightsIds = new HashSet<>(deleteFlightsRequest.getFlightsIds());
            Map<String, String> reservationCodeToEmail = new HashMap<>();
            Map<String, String> reservationCodeToAirlineName = new HashMap<>();
            for (Long flightId : deleteFlightsIds) {
                Flight flight = getFlightById(flightId);
                for (Passenger passenger : flight.getPassengers()) {
                    reservationsIds.add(passenger.getReservation().getReservationId());
                    if (!reservationCodeToEmail.containsKey(passenger.getReservation().getReservationCode())) {
                        reservationCodeToEmail.put(
                                passenger.getReservation().getReservationCode(),
                                passenger.getReservation().getEmail());
                        reservationCodeToAirlineName.put(
                                passenger.getReservation().getReservationCode(),
                                passenger.getFlight().getAircraft().getAirline().getAirlineName()
                        );
                    }
                }
                flightRepository.deleteById(flightId);
            }
            for (Long reservationId : reservationsIds) {
                reservationService.deleteReservation(reservationId);
            }

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails)principal).getUsername();
            var user = userRepository.findByEmail(email);

            for (Map.Entry<String, String> entry : reservationCodeToEmail.entrySet()) {
                var mailRequestEntity = mailRequestRepository.save(
                        MailRequest.builder()
                                .createdAt(Instant.now())
                                .status(MailRequestStatus.SENT)
                                .payload("")
                                .user(user)
                                .build()
                );

                var mailRequest = MailServiceRequest.builder()
                        .mailRequestId(mailRequestEntity.getId())
                        .email(entry.getValue())
                        .subject(reservationCodeToAirlineName.get(entry.getKey()))
                        .text(buildDeleteFlight(entry.getKey()))
                        .build();
                mailRequestEntity.setPayload(mapper.writeValueAsString(mailRequest));
                mailRequestRepository.save(mailRequestEntity);

                stompController.send(
                        "process-mail-message",
                        mailRequest
                );
            }
            bitronixTransactionManager.commit();
            return deleteFlightsIds;
        } catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("deleting flights - " + e.getMessage());
        }
    }

    private String buildDeleteFlight(String reservationCode) {
        return "<b>Ваши билеты на aviasales.ru по заказу " + reservationCode + " больше недействительны </b>,<br>" +
                "<i>Произошла отмена рейса </br>" +
                "Обратитесь на сайт за возвратом </br> </i>";
    }
}
