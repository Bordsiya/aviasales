package com.example.aviasales.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class FlightQuery {
    private Long flightId;
    private Long minPrice;
    private List<Long> tariffIds;
}
