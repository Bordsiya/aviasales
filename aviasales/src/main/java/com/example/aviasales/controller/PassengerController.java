package com.example.aviasales.controller;

import com.example.aviasales.dto.AddPassengersDTO;
import com.example.aviasales.entity.Passenger;
import com.example.aviasales.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/passengers")
@Tag(name = "Контроллер пассажиров(билетов)", description = "Описание пассажиров(с информацией о выбранных полетах)")
public class PassengerController {

    private PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @Operation(
            summary = "Бронирование билета",
            description = "Добавление новых пассажиров"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Passenger>> addPassengers(
            @Validated @RequestBody @Parameter(description = "Запрос добавления пассажира") AddPassengersDTO addPassengersDTO
            ){
        return ResponseEntity.ok(passengerService.addPassengers(addPassengersDTO));
    }
}
