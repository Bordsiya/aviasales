package com.example.aviasales.dto.search_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SearchResponseAirportDTO {
    private Long airportId;
    private String airportCode;
    private String airportName;
    private String airportCity;
    private String airportState;
    private String airportCountry;
}
