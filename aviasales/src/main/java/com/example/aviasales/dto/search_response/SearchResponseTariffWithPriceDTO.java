package com.example.aviasales.dto.search_response;

import com.example.aviasales.entity.Tariff;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность ответа о тарифе и цены поездки с ним")
public class SearchResponseTariffWithPriceDTO {
    @Schema(description = "Описание тарифа")
    private Tariff tariff;
    @Schema(description = "Цена на полёт с этим тарифом", example = "12000")
    private Long price;
}
