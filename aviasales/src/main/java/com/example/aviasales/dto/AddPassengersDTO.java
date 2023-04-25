package com.example.aviasales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class AddPassengersDTO {
    @NotNull(message = "Passengers cannot be null.")
    @NotEmpty(message = "Passengers required")
    private Set<PassengerDTO> passengers;
    @NotBlank(message = "Phone-number is required.")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Phone-number must start with +7, then - 10 numbers more.")
    private String phoneNumber;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be correct.")
    private String email;
    @NotNull(message = "Flight-id cannot be null.")
    @Min(1)
    private Long flightId;
}
