package com.example.aviasales.service;

import com.example.aviasales.dto.SearchRequestDTO;
import com.example.aviasales.dto.search_response.SearchResponseDTO;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.exception.not_found.FlightNotFoundException;
import com.example.aviasales.repo.FlightRepository;
import com.example.aviasales.repo.FlightQueryRepository;
import com.example.aviasales.util.FlightQuery;
import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.mappers.SearchResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private FlightRepository flightRepository;

    private FlightQueryRepository flightQueryRepository;
    private TariffService tariffService;
    private SearchResponseMapper searchResponseMapper;
    @Autowired
    public FlightService(
            FlightRepository flightRepository,
            FlightQueryRepository flightQueryRepository,
            TariffService tariffService,
            SearchResponseMapper searchResponseMapper
    ) {
        this.flightRepository = flightRepository;
        this.flightQueryRepository = flightQueryRepository;
        this.tariffService = tariffService;
        this.searchResponseMapper = searchResponseMapper;
    }

    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId).orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    public Set<SearchResponseDTO> getFlightsFiltered(SearchRequestDTO searchRequestDTO) {
        List<FlightQuery> filteredFlights = flightQueryRepository.findFlightQueriesByFilters(
                searchRequestDTO.getAirportFromId(),
                searchRequestDTO.getAirportToId(),
                searchRequestDTO.getDateFrom(),
                searchRequestDTO.getAmountOfAdults(),
                searchRequestDTO.getAmountOfChildren(),
                searchRequestDTO.getTariff(),
                searchRequestDTO.getHasBaggage(),
                searchRequestDTO.getDepartureTimeFrom(),
                searchRequestDTO.getArrivalTimeFrom(),
                searchRequestDTO.getFlightDurationTimeUntilInHH(),
                searchRequestDTO.getMaxPrice(),
                SortingAlgorithm.valueOf(searchRequestDTO.getSortingAlgorithm()),
                searchRequestDTO.getPageSize(),
                searchRequestDTO.getPageSize() * searchRequestDTO.getPageNumber()
        );
        if (searchRequestDTO.getDateBack() != null) {
            filteredFlights.addAll(flightQueryRepository.findFlightQueriesByFilters(
                    searchRequestDTO.getAirportFromId(),
                    searchRequestDTO.getAirportToId(),
                    searchRequestDTO.getDateBack(),
                    searchRequestDTO.getAmountOfAdults(),
                    searchRequestDTO.getAmountOfChildren(),
                    searchRequestDTO.getTariff(),
                    searchRequestDTO.getHasBaggage(),
                    searchRequestDTO.getDepartureTimeFrom(),
                    searchRequestDTO.getArrivalTimeFrom(),
                    searchRequestDTO.getFlightDurationTimeUntilInHH(),
                    searchRequestDTO.getMaxPrice(),
                    SortingAlgorithm.valueOf(searchRequestDTO.getSortingAlgorithm()),
                    searchRequestDTO.getPageSize(),
                    searchRequestDTO.getPageSize() * searchRequestDTO.getPageNumber()
            ));
        }
        Set<SearchResponseDTO> searchResponseDTOS = new HashSet<>();
        for (FlightQuery flightQuery: filteredFlights) {
            Flight flight = getFlightById(flightQuery.getFlightId());
            Set<Tariff> tariffs = flightQuery.getTariffIds().stream()
                    .map(tariffId -> tariffService.getTariffById(tariffId))
                    .collect(Collectors.toSet());
            searchResponseDTOS.add(
                    searchResponseMapper.toDTO(
                            flightQuery.getMinPrice(),
                            flight.getAircraft().getAirline(),
                            flight,
                            flight.getDepartureAirport(),
                            flight.getArrivalAirport(),
                            tariffs,
                            flight.getAircraft()
                    )
            );
        }

        return searchResponseDTOS;
    }
}
