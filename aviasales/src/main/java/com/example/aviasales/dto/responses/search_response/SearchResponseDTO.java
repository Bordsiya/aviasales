package com.example.aviasales.dto.responses.search_response;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность ответа на поисковой запрос")
public class SearchResponseDTO {
    @JsonView
    @Schema(description = "Описание авиакомпании при поиске")
    private SearchResponseAirlineDTO airline;
    @JsonView
    @Schema(description = "Описание полёта при поиске")
    private SearchResponseFlightDTO flight;
    @JsonView
    @Schema(description = "Описание аэропорта отправления при поиске")
    private SearchResponseAirportDTO departureAirport;
    @JsonView
    @Schema(description = "Описание аэропорта прибытия при поиске")
    private SearchResponseAirportDTO arrivalAirport;
    @JsonView
    @Schema(description = "Описание самолёта при поиске")
    private SearchResponseAircraftDTO aircraft;
    @JsonView
    @Schema(description = "Описание тарифов с ценами на полет при поиске")
    private Set<SearchResponseTariffWithPriceDTO> tariffsWithPrices;
}
