package com.example.aviasales.dto.requests;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.aviasales.dto.PassengerDTO;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность бронирования (добавления пассажиров на рейс)")
public class AddPassengersDTO {
    @NotNull(message = "Passengers cannot be null.")
    @NotEmpty(message = "Passengers required")
    @JsonView
    @Schema(description = "Множество пассажиров")
    private Set<PassengerDTO> passengers;
    @NotBlank(message = "Phone-number is required.")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Phone-number must start with +7, then - 10 numbers more.")
    @JsonView
    @Schema(description = "Номер телефона", example = "+79527977526")
    private String phoneNumber;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be correct.")
    @JsonView
    @Schema(description = "Email адрес", example = "aya.nikiforova@mail.ru")
    private String email;
    @NotNull(message = "Flight-id cannot be null.")
    @Min(1)
    @JsonView
    @Schema(description = "ID полёта", example = "1")
    private Long flightId;
}
