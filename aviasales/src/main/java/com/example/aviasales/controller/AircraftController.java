package com.example.aviasales.controller;

import com.example.aviasales.dto.requests.AddAircraftsDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.service.AircraftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping(value = "/public/aircrafts")
@Tag(name = "Контроллер самолётов", description = "Описание самолётов")
@SecurityRequirement(name = "basicAuth")
public class AircraftController {
    private AircraftService aircraftService;

    @Autowired
    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @Operation(
            summary = "Получение информации о самолёте по его ID",
            description = "Позволяет получить информацию о самолёте"
    )
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aircraft> getAircraftById(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "ID самолёта") Long id
    ) {
        return ResponseEntity.ok(aircraftService.getAircraftById(id));
    }

}
