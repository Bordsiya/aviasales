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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Schema(description = "Сущность поискового запроса полетов")
public class SearchRequestDTO {

    public SearchRequestDTO (
            Long airportFromId,
            Long airportToId,
            LocalDate dateFrom,
            LocalDate dateBack,
            Long amountOfAdults,
            Long amountOfChildren,
            String tariff,
            Boolean hasBaggage,
            LocalTime departureTimeFrom,
            LocalTime arrivalTimeFrom,
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
        this.hasBaggage = (hasBaggage == null ? Boolean.FALSE : hasBaggage);
        this.departureTimeFrom = (departureTimeFrom == null ? LocalTime.MIN : departureTimeFrom);
        this.arrivalTimeFrom = (arrivalTimeFrom == null ? LocalTime.MIN : arrivalTimeFrom);
        this.flightDurationTimeUntilInHH = (flightDurationTimeUntilInHH == null ? 32L : flightDurationTimeUntilInHH);
        this.maxPrice = (maxPrice == null ? 40000L : maxPrice);
        this.sortingAlgorithm = (sortingAlgorithm == null ? SortingAlgorithm.CHEAP_FIRST.name() : sortingAlgorithm);
        this.pageNumber = (pageNumber == null ? 1 : pageNumber);
        this.pageSize = (pageSize == null ? 10 : pageSize);
    }
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
    private Boolean hasBaggage;
    @JsonView
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime departureTimeFrom;
    @JsonView
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime arrivalTimeFrom;
    @Min(0)
    @JsonView
    private Long flightDurationTimeUntilInHH;
    @Min(0)
    @JsonView
    private Long maxPrice;
    @ValueOfEnum(enumClass = SortingAlgorithm.class)
    @JsonView
    private String sortingAlgorithm;
    @Min(1)
    @JsonView
    private Integer pageNumber;
    @Min(1)
    @JsonView
    private Integer pageSize;
}
