package com.example.aviasales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ReservationDTO {
    @NotBlank(message = "Reservation-code is required.")
    private String reservationCode;
    @NotBlank(message = "Phone-number is required.")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Phone-number must start with +7, then - 10 numbers more.")
    private String phoneNumber;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be correct.")
    private String email;
}
