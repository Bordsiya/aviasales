package com.example.aviasales.dto.search_response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность ответа о полёте")
public class SearchResponseFlightDTO {
    @Schema(description = "ID полёта", example = "1")
    private Long flightId;
    @Schema(description = "Дата отправления", example = "2023-07-21")
    private LocalDate departureDate;
    @Schema(description = "Дата прибытия", example = "2023-07-21")
    private LocalDate arrivalDate;
    @Schema(description = "Время отправления", example = "10:00")
    private LocalTime departureTime;
    @Schema(description = "Время прибытия", example = "13:00")
    private LocalTime arrivalTime;
    @Schema(description = "Общая цена для детей", example = "5000")
    private Long defaultPriceForKids;
    @Schema(description = "Общая цена для взрослых", example = "7000")
    private Long defaultPriceForAdults;
    @Schema(description = "Время в полёте (в часах)", example = "5")
    private Duration flightDurationTime;
}
