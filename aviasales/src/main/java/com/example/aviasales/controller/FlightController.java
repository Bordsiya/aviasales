package com.example.aviasales.controller;

import com.example.aviasales.dto.requests.AddAirlinesDTO;
import com.example.aviasales.dto.requests.AddFlightsDTO;
import com.example.aviasales.dto.requests.SearchRequestDTO;
import com.example.aviasales.dto.search_response.SearchResponseDTO;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.service.FlightService;
import com.example.aviasales.util.mappers.SearchRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/public/flights")
@Tag(name = "Контроллер полетов", description = "Описание полетов")
@SecurityRequirement(name = "basicAuth")
public class FlightController {

    private FlightService flightService;
    private SearchRequestMapper searchRequestMapper;

    @Autowired
    public FlightController(FlightService flightService, SearchRequestMapper searchRequestMapper) {
        this.flightService = flightService;
        this.searchRequestMapper = searchRequestMapper;
    }

    @Operation(
            summary = "Получение информации о полете по его ID",
            description = "Позволяет получить информацию о полете"
    )
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flight> getFlightById(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "ID полета") Long id
    ) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @Operation(
            summary = "Получение списка предпочтительных рейсов",
            description = "Позволяет по заданным фильтрам получить список предпочтительных полетов"
    )
    @GetMapping(path = "/search-flights", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchResponseDTO>> searchFlights(
            @Validated @RequestBody SearchRequestDTO searchRequestDTO
    ) {
        return ResponseEntity.ok(flightService.getFlightsFiltered(searchRequestMapper.fromDTO(searchRequestDTO)));
    }
}
