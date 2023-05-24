package com.example.aviasales.dto;

import com.example.aviasales.entity.Flight;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AircraftDTO {
    @NotBlank(message = "Manufacturer is required.")
    @JsonView
    private String manufacturer;

    @NotBlank(message = "Model is required.")
    @JsonView
    private String model;

    @NotNull(message = "Number-of-seats cannot be null.")
    @Min(1)
    @JsonView
    private Integer numberOfSeats;

    @NotNull(message = "Airline-id cannot be null.")
    @Min(1)
    @JsonView
    private Long airlineId;
}
