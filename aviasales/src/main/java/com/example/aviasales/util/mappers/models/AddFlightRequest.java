package com.example.aviasales.util.mappers.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class AddFlightRequest {
    private Long departureAirportId;

    private Long arrivalAirportId;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private Long defaultPriceForKids;

    private Long defaultPriceForAdults;

    private Long aircraftId;
}
