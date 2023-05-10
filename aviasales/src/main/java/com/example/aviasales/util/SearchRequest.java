package com.example.aviasales.util;

import com.example.aviasales.util.annotations.ValueOfEnum;
import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.enums.TariffType;
import com.example.aviasales.util.mappers.SearchRequestMapper;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SearchRequest {
    public SearchRequest(
            Long airportFromId,
            Long airportToId,
            LocalDate dateFrom,
            LocalDate dateBack,
            Long amountOfAdults,
            Long amountOfChildren,
            String tariff,
            Boolean hasBaggage,
            String departureTimeFrom,
            String arrivalTimeFrom,
            Long flightDurationTimeUntilInHH,
            Long maxPrice,
            String sortingAlgorithm,
            Integer pageNumber,
            Integer pageSize
    ) {
        this.airportFromId = airportFromId;
        this.airportToId = airportToId;
        this.dateFrom = dateFrom;
        this.dateBack = dateBack;
        this.amountOfAdults = amountOfAdults;
        this.amountOfChildren = amountOfChildren;
        this.tariff = tariff;
        this.hasBaggage = hasBaggage;
        this.departureTimeFrom = LocalTime.parse(departureTimeFrom);
        this.arrivalTimeFrom = LocalTime.parse(arrivalTimeFrom);
        this.flightDurationTimeUntilInHH = flightDurationTimeUntilInHH;
        this.maxPrice = maxPrice;
        this.sortingAlgorithm = sortingAlgorithm;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    private Long airportFromId;
    private Long airportToId;
    private LocalDate dateFrom;
    private LocalDate dateBack;
    private Long amountOfAdults;
    private Long amountOfChildren;
    private String tariff;
    private Boolean hasBaggage;
    private LocalTime departureTimeFrom;
    private LocalTime arrivalTimeFrom;
    private Long flightDurationTimeUntilInHH;
    private Long maxPrice;
    private String sortingAlgorithm;
    private Integer pageNumber;
    private Integer pageSize;
}
