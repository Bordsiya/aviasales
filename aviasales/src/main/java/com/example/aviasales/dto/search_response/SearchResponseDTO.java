package com.example.aviasales.dto.search_response;

import com.example.aviasales.entity.Tariff;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class SearchResponseDTO {
    @JsonView
    private Long minPrice;
    @JsonView
    private SearchResponseAirlineDTO airline;
    @JsonView
    private SearchResponseFlightDTO flight;
    @JsonView
    private SearchResponseAirportDTO departureAirport;
    @JsonView
    private SearchResponseAirportDTO arrivalAirport;
    @JsonView
    private SearchResponseAircraftDTO aircraft;
    @JsonView
    private Set<Tariff> tariffs;
}
