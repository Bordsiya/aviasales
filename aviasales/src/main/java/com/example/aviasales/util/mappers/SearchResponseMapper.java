package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.responses.search_response.*;
import com.example.aviasales.entity.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@Component
public class SearchResponseMapper {
    public SearchResponseDTO toDTO(
            Airline airline,
            Flight flight,
            Airport departureAirport,
            Airport arrivalAirport,
            Set<SearchResponseTariffWithPriceDTO> tariffsWithPrices,
            Aircraft aircraft
    ) {
        return new SearchResponseDTO(
                new SearchResponseAirlineDTO(
                        airline.getAirlineId(),
                        airline.getAirlineName()
                ),
                new SearchResponseFlightDTO(
                        flight.getFlightId(),
                        flight.getDepartureDate(),
                        flight.getArrivalDate(),
                        flight.getDepartureTime(),
                        flight.getArrivalTime(),
                        flight.getDefaultPriceForKids(),
                        flight.getDefaultPriceForAdults(),
                        Duration.between(flight.getDepartureTime(), flight.getArrivalTime())
                ),
                new SearchResponseAirportDTO(
                        departureAirport.getAirportId(),
                        departureAirport.getAirportCode(),
                        departureAirport.getAirportName(),
                        departureAirport.getCity(),
                        departureAirport.getState(),
                        departureAirport.getCountry()
                ),
                new SearchResponseAirportDTO(
                        arrivalAirport.getAirportId(),
                        arrivalAirport.getAirportCode(),
                        arrivalAirport.getAirportName(),
                        arrivalAirport.getCity(),
                        arrivalAirport.getState(),
                        arrivalAirport.getCountry()
                ),
                new SearchResponseAircraftDTO(
                        aircraft.getAircraftId(),
                        aircraft.getModel()
                ),
                tariffsWithPrices
        );
    }

}
