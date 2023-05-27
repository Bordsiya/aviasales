package com.example.aviasales.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Сущность пользователя")
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be correct.")
    @JsonView
    @Schema(description = "Email адрес", example = "aya.nikiforova@mail.ru")
    private String email;
    @NotBlank(message = "Password is required.")
    @JsonView
    private String password;
}
