package com.example.aviasales.dto;

import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.enums.TariffType;
import com.example.aviasales.util.annotations.ValueOfEnum;
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
        this.hasBaggage = (hasBaggage == null ? Boolean.FALSE : hasBaggage);
        this.departureTimeFrom = (departureTimeFrom == null ? "00:00" : departureTimeFrom);
        this.arrivalTimeFrom = (arrivalTimeFrom == null ? "00:00" : arrivalTimeFrom);
        this.flightDurationTimeUntilInHH = (flightDurationTimeUntilInHH == null ? 32L : flightDurationTimeUntilInHH);
        this.maxPrice = (maxPrice == null ? 40000L : maxPrice);
        this.sortingAlgorithm = (sortingAlgorithm == null ? SortingAlgorithm.CHEAP_FIRST.name() : sortingAlgorithm);
        this.pageNumber = (pageNumber == null ? 1 : pageNumber);
        this.pageSize = (pageSize == null ? 10 : pageSize);
    }
    @NotNull(message = "The city-from cannot be null.")
    @JsonView
    @Schema(description = "ID аэропорта отправления", example = "1")
    private Long airportFromId;
    @NotNull(message = "The city-to cannot be null.")
    @JsonView
    @Schema(description = "ID аэропорта прибытия", example = "2")
    private Long airportToId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonView
    @NotNull(message = "The date-from cannot be null.")
    @Schema(description = "Дата отправления", example = "2023-02-09")
    private LocalDate dateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonView
    @Schema(description = "Дата прибытия", example = "2023-02-10")
    private LocalDate dateBack;
    @Min(0)
    @NotNull(message = "Amount-of-adults cannot be null.")
    @JsonView
    @Schema(description = "Количество взрослых", example = "2")
    private Long amountOfAdults;
    @Min(0)
    @NotNull(message = "Amount-of-children cannot be null.")
    @JsonView
    @Schema(description = "Количество детей", example = "1")
    private Long amountOfChildren;
    @NotBlank(message = "Tariff is required.")
    @ValueOfEnum(enumClass = TariffType.class)
    @JsonView
    @Schema(description = "Тип тарифа", example = "ECONOM")
    private String tariff;
    @JsonView
    @Schema(description = "Имеется ли багаж", example = "true")
    private Boolean hasBaggage;
    @JsonView
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Schema(description = "Время отправления (начиная с)", example = "10:00")
    private String departureTimeFrom;
    @JsonView
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Schema(description = "Время прибытия (начиная с)", example = "14:00")
    private String arrivalTimeFrom;
    @Min(0)
    @JsonView
    @Schema(description = "Длительность полёта (в часах)", example = "4")
    private Long flightDurationTimeUntilInHH;
    @Min(0)
    @JsonView
    @Schema(description = "Максимальная цена", example = "40000")
    private Long maxPrice;
    @ValueOfEnum(enumClass = SortingAlgorithm.class)
    @JsonView
    @Schema(description = "Алгоритм сортировки результата", example = "CHEAP_FIRST")
    private String sortingAlgorithm;
    @Min(1)
    @JsonView
    @Schema(description = "Номер страницы", example = "1")
    private Integer pageNumber;
    @Min(1)
    @JsonView
    @Schema(description = "Максимальное количество записей на 1 странице", example = "10")
    private Integer pageSize;
}
