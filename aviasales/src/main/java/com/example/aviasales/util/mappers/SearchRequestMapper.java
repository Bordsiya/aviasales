package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.requests.SearchRequestDTO;
import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.mappers.models.SearchRequest;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

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
                (searchRequestDTO.getHasBaggage() == null ? Boolean.FALSE : searchRequestDTO.getHasBaggage()),
                (searchRequestDTO.getDepartureTimeFrom() == null
                        ? LocalTime.parse("00:00") : LocalTime.parse(searchRequestDTO.getDepartureTimeFrom())),
                (searchRequestDTO.getArrivalTimeFrom() == null
                        ? LocalTime.parse("00:00") : LocalTime.parse(searchRequestDTO.getArrivalTimeFrom())),
                (searchRequestDTO.getFlightDurationTimeUntilInHH() == null ? 32L : searchRequestDTO.getFlightDurationTimeUntilInHH()),
                (searchRequestDTO.getMaxPrice() == null ? 40000L : searchRequestDTO.getMaxPrice()),
                (searchRequestDTO.getSortingAlgorithm() == null
                        ? SortingAlgorithm.CHEAP_FIRST.name() : searchRequestDTO.getSortingAlgorithm()),
                (searchRequestDTO.getPageNumber() == null ? 1 : searchRequestDTO.getPageNumber()),
                (searchRequestDTO.getPageSize() == null ? 10 : searchRequestDTO.getPageSize())
        );
    }
}
