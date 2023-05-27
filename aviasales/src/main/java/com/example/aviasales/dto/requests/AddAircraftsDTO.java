package com.example.aviasales.dto.requests;

import com.example.aviasales.dto.AircraftDTO;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddAircraftsDTO {
    @NotNull(message = "Aircrafts cannot be null.")
    @NotEmpty(message = "Aircrafts required.")
    @JsonView
    private List<AircraftDTO> aircrafts;
}
