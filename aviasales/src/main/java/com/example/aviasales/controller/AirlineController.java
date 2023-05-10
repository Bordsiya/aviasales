package com.example.aviasales.controller;

import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.service.AirlineService;
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
import java.util.Set;

@RestController
@RequestMapping(value = "/airlines")
@Tag(name = "Контроллер авиакомпаний", description = "Описание авиакомпаний")
public class AirlineController {
    private AirlineService airlineService;
    @Autowired
    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @Operation(
            summary = "Получение информации об авиакомпании по её ID",
            description = "Позволяет получить информацию об авиакомпании"
    )
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Airline> getAirlineById(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "ID авиакомпании") Long id
    ) {
        return ResponseEntity.ok(airlineService.getAirlineById(id));
    }

    @Operation(
            summary = "Получение информации о всех тарифах авиакомпании по её ID",
            description = "Позволяет получить информацию о всех тарифах авиакомпании"
    )
    @GetMapping(path = "/{id}/tariffs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Tariff>> getAirlineTariffsByAirlineId(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "ID авиакомпании") Long id
    ) {
        return ResponseEntity.ok(airlineService.getAirlineTariffsByAirlineId(id));
    }
}
