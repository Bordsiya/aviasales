package com.example.aviasales.dto.search_response;

import com.example.aviasales.entity.Tariff;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SearchResponseTariffWithPriceDTO {
    private Tariff tariff;
    private Long price;
}
