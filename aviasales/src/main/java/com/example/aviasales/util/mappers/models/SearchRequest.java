package com.example.aviasales.util.mappers.models;

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
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SearchRequest {
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
