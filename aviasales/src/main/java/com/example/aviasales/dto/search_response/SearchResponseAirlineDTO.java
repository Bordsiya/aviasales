package com.example.aviasales.dto.search_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SearchResponseAirlineDTO {
    private Long airlineId;
    private String airlineName;
}
