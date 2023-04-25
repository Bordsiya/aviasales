package com.example.aviasales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность бронирования")
public class ReservationDTO {
    @NotBlank(message = "Reservation-code is required.")
    @Schema(description = "Код бронирования", example = "vQEF6")
    private String reservationCode;
    @NotBlank(message = "Phone-number is required.")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Phone-number must start with +7, then - 10 numbers more.")
    @Schema(description = "Номер телефона", example = "+79527977528")
    private String phoneNumber;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be correct.")
    @Schema(description = "Email адрес", example = "aya.nikiforova@mail.ru")
    private String email;
}
