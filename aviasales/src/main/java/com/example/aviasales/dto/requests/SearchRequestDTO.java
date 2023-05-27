package com.example.aviasales.dto.requests;

import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.enums.TariffType;
import com.example.aviasales.util.annotations.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность поискового запроса полетов")
public class SearchRequestDTO {
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
    @Schema(description = "Время отправления (начиная с)", example = "10:00")
    private String departureTimeFrom;
    @JsonView
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
