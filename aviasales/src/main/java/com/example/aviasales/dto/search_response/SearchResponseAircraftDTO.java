package com.example.aviasales.dto.search_response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность ответа о самолёте")
public class SearchResponseAircraftDTO {
    @Schema(description = "ID самолёта", example = "1")
    private Long aircraftId;
    @Schema(description = "Модель самолёта", example = "Heinkel He 111")
    private String aircraftModel;
}
