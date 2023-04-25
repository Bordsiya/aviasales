package com.example.aviasales.dto;

import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.enums.TariffType;
import com.example.aviasales.util.annotations.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
public class SearchRequestDTO {
    @NotNull(message = "The city-from cannot be null.")
    @JsonView
    private Long airportFromId;
    @NotNull(message = "The city-to cannot be null.")
    @JsonView
    private Long airportToId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonView
    @NotNull(message = "The date-from cannot be null.")
    private LocalDate dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonView
    private LocalDate dateBack;
    @Min(0)
    @NotNull(message = "Amount-of-adults cannot be null.")
    @JsonView
    private Long amountOfAdults;
    @Min(0)
    @NotNull(message = "Amount-of-children cannot be null.")
    @JsonView
    private Long amountOfChildren;
    @NotBlank(message = "Tariff is required.")
    @ValueOfEnum(enumClass = TariffType.class)
    @JsonView
    private String tariff;
    @JsonView
    private Boolean hasBaggage = Boolean.FALSE;
    @JsonView
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime departureTimeFrom = LocalTime.MIN;
    @JsonView
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime arrivalTimeFrom = LocalTime.MIN;
    @Min(0)
    @JsonView
    private Long flightDurationTimeUntilInHH = 32L;
    @Min(0)
    @JsonView
    private Long maxPrice = 400000L;
    @ValueOfEnum(enumClass = SortingAlgorithm.class)
    @JsonView
    private String sortingAlgorithm = SortingAlgorithm.CHEAP_FIRST.name();
    @Min(0)
    @JsonView
    private Integer pageNumber = 0;
    @Min(0)
    @JsonView
    private Integer pageSize = 10;
}
