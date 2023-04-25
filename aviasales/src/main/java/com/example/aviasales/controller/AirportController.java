package com.example.aviasales.controller;

import com.example.aviasales.entity.Airport;
import com.example.aviasales.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/airports")
@Tag(name = "Контроллер аэропортов", description = "Описание аэропортов")
public class AirportController {
    private AirportService airportService;
    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @Operation(
            summary = "Получение информации об аэропорте по его ID",
            description = "Позволяет получить информацию об аэропорте"
    )
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Airport> getAirportById(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "ID аэропорта") Long id
    ) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }
}
