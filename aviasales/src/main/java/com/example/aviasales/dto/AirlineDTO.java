package com.example.aviasales.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AirlineDTO {
    @JsonView
    @NotBlank(message = "Airline-name is required.")
    private String airlineName;
}
