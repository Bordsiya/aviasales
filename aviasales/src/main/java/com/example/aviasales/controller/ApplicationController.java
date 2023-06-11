package com.example.aviasales.controller;

import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.entity.Application;
import com.example.aviasales.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequestMapping(value = "/public/applications")
@Tag(name = "Контроллер заявок", description = "Составление заявок на действия для рассмотрения их далее админом")
public class ApplicationController {
    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Operation(
            summary = "Составление заявки на отказ от билета для пассажира",
            description = "Заявка на удаление пассажира"
    )
    @PostMapping(path = "/delete-passenger/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    //@MessageMapping("/application.create")
    public ResponseEntity<Application> deletePassengerApplication(
            @PathVariable @Min(1) Long id, Principal principal
    ){
        return ResponseEntity.ok(applicationService.addApplicationDeletePassenger(id, principal));
    }

    @Operation(
            summary = "Составление заявки на изменение данных о пассажире",
            description = "Заявка на изменение данных пассажира"
    )
    @PostMapping(path = "/update-passenger/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    //@MessageMapping("/application.create")
    public ResponseEntity<Application> updatePassengerApplication(
            @PathVariable @Min(1) Long id, @Valid @RequestBody PassengerDTO passengerDTO, Principal principal
    ){
        return ResponseEntity.ok(applicationService.addApplicationUpdatePassenger(id, passengerDTO, principal));
    }
}
