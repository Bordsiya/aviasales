package com.example.aviasales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность бронирования (добавления пассажиров на рейс)")
public class AddPassengersDTO {
    @NotNull(message = "Passengers cannot be null.")
    @NotEmpty(message = "Passengers required")
    @Schema(description = "Множество пассажиров")
    private Set<PassengerDTO> passengers;
    @NotBlank(message = "Phone-number is required.")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Phone-number must start with +7, then - 10 numbers more.")
    @Schema(description = "Номер телефона", example = "+79527977526")
    private String phoneNumber;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be correct.")
    @Schema(description = "Email адрес", example = "aya.nikiforova@mail.ru")
    private String email;
    @NotNull(message = "Flight-id cannot be null.")
    @Min(1)
    @Schema(description = "ID полёта", example = "1")
    private Long flightId;
}
