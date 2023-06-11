package com.example.aviasales.controller;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.dto.requests.*;
import com.example.aviasales.entity.*;
import com.example.aviasales.service.*;
import com.example.aviasales.util.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/private")
@Tag(name = "Контроллер администратора", description = "Описание администраторских действий")
@SecurityRequirement(name = "basicAuth")
public class AdminController {
    private AircraftService aircraftService;
    private AirlineService airlineService;
    private FlightService flightService;
    private PassengerService passengerService;
    private ApplicationService applicationService;

    @Autowired
    public AdminController(AircraftService aircraftService,
                           AirlineService airlineService,
                           FlightService flightService,
                           PassengerService passengerService,
                           ApplicationService applicationService) {
        this.aircraftService = aircraftService;
        this.airlineService = airlineService;
        this.flightService = flightService;
        this.passengerService = passengerService;
        this.applicationService = applicationService;
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
            @Valid @RequestBody DeleteFlightsRequest deleteFlightsRequest
            ) {
        return ResponseEntity.ok(flightService.deleteFlights(deleteFlightsRequest));
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

    @GetMapping(path = "/applications/search-by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Application>> getApplicationsByStatus(
            @RequestParam String applicationStatus
    ) {
        return ResponseEntity.ok(applicationService.searchByStatus(ApplicationStatus.valueOf(applicationStatus)));
    }

    @GetMapping(path = "/applications/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> getApplicationById(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE) Long id
    ) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    /*
    @PostMapping
    public ResponseEntity<Long> setApplicationStatus(
            @Valid @RequestBody SetApplicationStatusRequestDTO setApplicationStatusRequestDTO
    ) {
        return ResponseEntity.ok(applicationService.changeApplicationStatus(setApplicationStatusRequestDTO));
    }

     */
}
