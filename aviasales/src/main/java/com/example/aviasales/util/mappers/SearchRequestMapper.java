package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.SearchRequestDTO;
import com.example.aviasales.util.SearchRequest;
import org.springframework.stereotype.Component;

@Component
public class SearchRequestMapper {
    public SearchRequest fromDTO(SearchRequestDTO searchRequestDTO) {
        return new SearchRequest(
                searchRequestDTO.getAirportFromId(),
                searchRequestDTO.getAirportToId(),
                searchRequestDTO.getDateFrom(),
                searchRequestDTO.getDateBack(),
                searchRequestDTO.getAmountOfAdults(),
                searchRequestDTO.getAmountOfChildren(),
                searchRequestDTO.getTariff(),
                searchRequestDTO.getHasBaggage(),
                searchRequestDTO.getDepartureTimeFrom(),
                searchRequestDTO.getArrivalTimeFrom(),
                searchRequestDTO.getFlightDurationTimeUntilInHH(),
                searchRequestDTO.getMaxPrice(),
                searchRequestDTO.getSortingAlgorithm(),
                searchRequestDTO.getPageNumber(),
                searchRequestDTO.getPageSize()
        );
    }
}
