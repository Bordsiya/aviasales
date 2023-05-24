package com.example.aviasales.dto.requests;

import com.example.aviasales.dto.AircraftDTO;
import com.example.aviasales.dto.AirlineDTO;
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
public class AddAirlinesDTO {
    @NotNull(message = "Airlines cannot be null.")
    @NotEmpty(message = "Airlines required.")
    @JsonView
    private Set<AirlineDTO> airlines;
}
