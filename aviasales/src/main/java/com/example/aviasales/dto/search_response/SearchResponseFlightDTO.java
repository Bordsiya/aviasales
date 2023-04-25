package com.example.aviasales.dto.search_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
public class SearchResponseFlightDTO {
    private Long flightId;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Long defaultPriceForKids;
    private Long defaultPriceForAdults;
    private Duration flightDurationTime;
}
