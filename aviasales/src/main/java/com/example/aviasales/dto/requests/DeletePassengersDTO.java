package com.example.aviasales.dto.requests;

import com.example.aviasales.dto.FlightDTO;
import com.example.aviasales.dto.LongIdDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
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
public class DeletePassengersDTO {
    @NotNull(message = "Passengers-ids cannot be null.")
    @NotEmpty(message = "Passengers-ids required.")
    @JsonView
    private Set<LongIdDTO> passengersIds;

}
