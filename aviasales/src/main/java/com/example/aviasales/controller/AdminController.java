package com.example.aviasales.controller;

import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.dto.requests.AddAircraftsDTO;
import com.example.aviasales.dto.requests.AddAirlinesDTO;
import com.example.aviasales.dto.requests.AddFlightsDTO;
import com.example.aviasales.dto.requests.DeletePassengersDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.entity.Passenger;
import com.example.aviasales.service.AircraftService;
import com.example.aviasales.service.AirlineService;
import com.example.aviasales.service.FlightService;
import com.example.aviasales.service.PassengerService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RestController
@RequestMapping(value = "/private")
@Tag(name = "Контроллер администратора", description = "Описание администраторских действий")
public class AdminController {
    private AircraftService aircraftService;
    private AirlineService airlineService;
    private FlightService flightService;
    private PassengerService passengerService;

    @Autowired
    public AdminController(AircraftService aircraftService,
                           AirlineService airlineService,
                           FlightService flightService,
                           PassengerService passengerService) {
        this.aircraftService = aircraftService;
        this.airlineService = airlineService;
        this.flightService = flightService;
        this.passengerService = passengerService;
    }

    @PostMapping(path = "/aircrafts/add-aircrafts",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Aircraft>> addAircrafts(
            @Valid @RequestBody AddAircraftsDTO addAircraftsDTO
    ) {
        return ResponseEntity.ok(aircraftService.addAircrafts(addAircraftsDTO));
    }

    @PostMapping(
            path = "/airlines/add-airlines",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Airline>> addAirlines(
            @Valid @RequestBody AddAirlinesDTO addAirlinesDTO
    ) {
        return ResponseEntity.ok(airlineService.addAirlines(addAirlinesDTO));
    }

    @PostMapping(
            path = "/flights/add-flights",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Flight>> addFlights(
            @Valid @RequestBody AddFlightsDTO addFlightsDTO
    ) {
        return ResponseEntity.ok(flightService.addFlights(addFlightsDTO));
    }

    @DeleteMapping(
            path = "/flights/delete-flights",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Set<Long>> deleteFlights(
            @Valid @NotNull @NotEmpty Set<Long> flightIds
    ) {
        return ResponseEntity.ok(flightService.deleteFlights(flightIds));
    }

    @DeleteMapping(
            path = "/passengers/delete-passengers",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Set<Long>> deletePassengers(
            @Valid @RequestBody DeletePassengersDTO deletePassengersDTO
            ) {
        return ResponseEntity.ok(passengerService.deletePassengers(deletePassengersDTO));
    }

    @PutMapping(
            path = "/passengers/update-passenger/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Passenger> updatePassenger(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE) Long id, @Valid @RequestBody PassengerDTO passengerDTO
    ) {
        return ResponseEntity.ok(passengerService.updatePassenger(id, passengerDTO));
    }


}