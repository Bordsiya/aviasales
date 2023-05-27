package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.FlightDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airport;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.entity.Passenger;
import com.example.aviasales.util.mappers.models.AddFlightRequest;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class FlightsMapper {
    public AddFlightRequest fromDTO(FlightDTO flightDTO) {
        return new AddFlightRequest(
                flightDTO.getDepartureAirportId(),
                flightDTO.getArrivalAirportId(),
                flightDTO.getDepartureDate(),
                flightDTO.getArrivalDate(),
                LocalTime.parse(flightDTO.getDepartureTime()),
                LocalTime.parse(flightDTO.getArrivalTime()),
                flightDTO.getDefaultPriceForKids(),
                flightDTO.getDefaultPriceForAdults(),
                flightDTO.getAircraftId()
        );
    }

    public Flight fromDTO(AddFlightRequest addFlightRequest,
                          Airport departureAirport,
                          Airport arrivalAirport,
                          Aircraft aircraft,
                          List<Passenger> passengers
                          ) {
        return new Flight(
                null,
                departureAirport,
                arrivalAirport,
                addFlightRequest.getDepartureDate(),
                addFlightRequest.getArrivalDate(),
                addFlightRequest.getDepartureTime(),
                addFlightRequest.getArrivalTime(),
                addFlightRequest.getDefaultPriceForKids(),
                addFlightRequest.getDefaultPriceForAdults(),
                aircraft,
                passengers
        );
    }
}
