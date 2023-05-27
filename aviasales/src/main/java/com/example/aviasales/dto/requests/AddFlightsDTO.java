package com.example.aviasales.dto.requests;

import com.example.aviasales.dto.AirlineDTO;
import com.example.aviasales.dto.FlightDTO;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddFlightsDTO {
    @NotNull(message = "Flights cannot be null.")
    @NotEmpty(message = "Flights required.")
    @JsonView
    private Set<FlightDTO> flights;
}
