package com.example.aviasales.util.sort;

import com.example.aviasales.dto.responses.search_response.SearchResponseDTO;
import com.example.aviasales.dto.responses.search_response.SearchResponseTariffWithPriceDTO;
import com.example.aviasales.util.enums.SortingAlgorithm;

import java.util.Collections;
import java.util.Comparator;

public class SearchResponseDTOSortUtils {
    public static Comparator<SearchResponseDTO> getComparatorBySortingAlgorithm(SortingAlgorithm sortingAlgorithm) {
        if (sortingAlgorithm.equals(SortingAlgorithm.CHEAP_FIRST)) {
            return Comparator.comparing(sr -> Collections.min(sr.getTariffsWithPrices(), Comparator.comparing(SearchResponseTariffWithPriceDTO::getPrice)).getPrice());
        }
        else if (sortingAlgorithm.equals(SortingAlgorithm.ARRIVAL_TIME)) {
            return Comparator.comparing(sr -> sr.getFlight().getArrivalTime());
        }
        else if (sortingAlgorithm.equals(SortingAlgorithm.DEPARTURE_TIME)) {
            return Comparator.comparing(sr -> sr.getFlight().getDepartureTime());
        }
        else throw new IllegalArgumentException("Illegal getComparatorBySortingAlgorithm argument.");
    }
}
