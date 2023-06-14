package com.example.aviasales.dto.responses.search_response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность ответа об аэропорте")
public class SearchResponseAirportDTO {
    @Schema(description = "ID аэропорта", example = "1")
    private Long airportId;
    @Schema(description = "Код аэропорта", example = "ATL")
    private String airportCode;
    @Schema(description = "Название аэропорта", example = "Международный аэропорт Хартсфилд-Джексон Атланта")
    private String airportName;
    @Schema(description = "Город нахождения аэропорта", example = "Атланта")
    private String airportCity;
    @Schema(description = "Область/Штат аэропорта", example = "Джорджия")
    private String airportState;
    @Schema(description = "Страна аэропорта", example = "США")
    private String airportCountry;
}
