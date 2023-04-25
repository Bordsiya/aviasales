package com.example.aviasales.dto.search_response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность ответа об авиалинии")
public class SearchResponseAirlineDTO {
    @Schema(description = "ID авиакомпании", example = "1")
    private Long airlineId;
    @Schema(description = "Название авиакомпании", example = "Аэрофлот")
    private String airlineName;
}
