package com.example.aviasales.service;

import com.example.aviasales.dto.FlightDTO;
import com.example.aviasales.dto.requests.AddFlightsDTO;
import com.example.aviasales.dto.requests.DeleteFlightsRequest;
import com.example.aviasales.dto.search_response.SearchResponseDTO;
import com.example.aviasales.dto.search_response.SearchResponseTariffWithPriceDTO;
import com.example.aviasales.entity.*;
import com.example.aviasales.exception.AircraftAlreadyInUseException;
import com.example.aviasales.exception.DepartureTimeAfterArrivalTimeException;
import com.example.aviasales.exception.FlightWithTheSameAirportsException;
import com.example.aviasales.exception.not_found.FlightNotFoundException;
import com.example.aviasales.repo.FlightRepository;
import com.example.aviasales.util.mappers.FlightsMapper;
import com.example.aviasales.util.mappers.models.AddFlightRequest;
import com.example.aviasales.util.mappers.models.SearchRequest;
import com.example.aviasales.util.Utils;
import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.mappers.SearchResponseMapper;
import com.example.aviasales.util.sort.SearchResponseDTOSortUtils;
import com.example.aviasales.util.sort.SearchResponseTariffWithPriceDTOSortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FlightService {
    private FlightRepository flightRepository;
    private AirportService airportService;
    private AircraftService aircraftService;
    private ReservationService reservationService;
    private EmailService emailService;
    private SearchResponseMapper searchResponseMapper;
    private FlightsMapper flightsMapper;
    @Autowired
    public FlightService(
            FlightRepository flightRepository,
            @Lazy AirportService airportService,
            @Lazy AircraftService aircraftService,
            @Lazy ReservationService reservationService,
            EmailService emailService,
            SearchResponseMapper searchResponseMapper,
            FlightsMapper flightsMapper
    ) {
        this.flightRepository = flightRepository;
        this.airportService = airportService;
        this.aircraftService = aircraftService;
        this.reservationService = reservationService;
        this.emailService = emailService;
        this.searchResponseMapper = searchResponseMapper;
        this.flightsMapper = flightsMapper;
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
        for (Flight flight: filteredFlights) {
            Set<Tariff> tariffs = flight.getAircraft().getAirline().getTariffs();
            Set<SearchResponseTariffWithPriceDTO> tariffsWithPrices = new TreeSet<>(SearchResponseTariffWithPriceDTOSortUtils.getComparator());
            for (Tariff tariff: tariffs) {
                long price = (flight.getDefaultPriceForKids() + tariff.getPriceForKids()) * searchRequest.getAmountOfChildren()
                        + (flight.getDefaultPriceForAdults() + tariff.getPriceForAdults()) * searchRequest.getAmountOfAdults();
                if (tariff.getTariffType().name().equals(searchRequest.getTariff())
                        && tariff.getHasBaggage().equals(searchRequest.getHasBaggage())
                        && price <= searchRequest.getMaxPrice()
                ) tariffsWithPrices.add(
                        new SearchResponseTariffWithPriceDTO(
                                tariff,
                                price
                        )
                );
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

    @Transactional(rollbackFor = Throwable.class)
    public Set<Flight> addFlights(AddFlightsDTO addFlightsDTO) {
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

            Long flightIdUsingAircraft = flightRepository.getFlightIdWithAircraftIdBetweenDepartureDateAndArrivalDate(
                    addFlightRequest.getAircraftId(), addFlightRequest.getDepartureDate(), addFlightRequest.getArrivalDate());
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
        return flights;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Set<Long> deleteFlights(DeleteFlightsRequest deleteFlightsRequest) {
        Set<Long> reservationsIds = new HashSet<>();
        Set<Long> deleteFlightsIds = new HashSet<>(deleteFlightsRequest.getFlightsIds());
        Map<String, String> reservationCodeToEmail = new HashMap<>();
        Map<String, String> reservationCodeToAirlineName = new HashMap<>();
        for (Long flightId:deleteFlightsIds) {
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
        for (Map.Entry<String, String> entry : reservationCodeToEmail.entrySet()) {
            sendDeleteFlightEmail(entry.getKey(), reservationCodeToAirlineName.get(entry.getKey()), entry.getValue());
        }
        return deleteFlightsIds;
    }

    private void sendDeleteFlightEmail(String reservationCode, String airlineName, String email) {
        String textBase = "<b>Ваши билеты на aviasales.ru по заказу " + reservationCode + " больше недействительны </b>,<br>" +
                "<i>Произошла отмена рейса </br>" +
                "Обратитесь на сайт за возвратом </br> </i>";
        emailService.sendHTMLMessage(
                email,
                airlineName,
                textBase
        );
    }


}
