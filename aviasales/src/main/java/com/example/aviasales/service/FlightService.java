package com.example.aviasales.service;

import com.example.aviasales.dto.search_response.SearchResponseDTO;
import com.example.aviasales.dto.search_response.SearchResponseTariffWithPriceDTO;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.exception.not_found.FlightNotFoundException;
import com.example.aviasales.repo.FlightRepository;
import com.example.aviasales.util.SearchRequest;
import com.example.aviasales.util.Utils;
import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.mappers.SearchResponseMapper;
import com.example.aviasales.util.sort.SearchResponseDTOSortUtils;
import com.example.aviasales.util.sort.SearchResponseTariffWithPriceDTOSortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlightService {
    private FlightRepository flightRepository;
    private SearchResponseMapper searchResponseMapper;
    @Autowired
    public FlightService(
            FlightRepository flightRepository,
            SearchResponseMapper searchResponseMapper
    ) {
        this.flightRepository = flightRepository;
        this.searchResponseMapper = searchResponseMapper;
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
}
