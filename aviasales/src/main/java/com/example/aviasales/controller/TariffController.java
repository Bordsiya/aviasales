package com.example.aviasales.controller;

import com.example.aviasales.entity.Tariff;
import com.example.aviasales.service.TariffService;
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
@RequestMapping(value = "/public/tariffs")
@Tag(name = "Контроллер тарифов", description = "Описание тарифов")
public class TariffController {
    private TariffService tariffService;

    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Operation(
            summary = "Получение информации о тарифе по его ID",
            description = "Позволяет получить информацию о тарифе"
    )
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tariff> getTariffById(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "ID тарифа") Long id
    ) {
        return ResponseEntity.ok(tariffService.getTariffById(id));
    }
}
