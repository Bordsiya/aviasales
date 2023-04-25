package com.example.aviasales.util.sort;

import com.example.aviasales.dto.search_response.SearchResponseTariffWithPriceDTO;

import java.util.Comparator;

public class SearchResponseTariffWithPriceDTOSortUtils {
    public static Comparator<SearchResponseTariffWithPriceDTO> getComparator() {
        return Comparator.comparing(SearchResponseTariffWithPriceDTO::getPrice);
    }
}
