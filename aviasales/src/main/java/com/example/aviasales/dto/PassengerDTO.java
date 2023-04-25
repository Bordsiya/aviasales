package com.example.aviasales.dto;

import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.enums.Gender;
import com.example.aviasales.util.annotations.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class PassengerDTO {
    @NotBlank(message = "First-name is required.")
    private String firstName;
    @NotBlank(message = "Last-name is required.")
    private String lastName;
    @NotBlank(message = "Patronymic is required.")
    private String patronymic;
    @NotBlank(message = "Gender is required.")
    @ValueOfEnum(enumClass = Gender.class)
    private String gender;
    @NotBlank(message = "Citizenship is required.")
    private String citizenship;
    @NotNull(message = "Is-kid cannot be null.")
    private Boolean isKid;
    @NotBlank(message = "Document-type is required.")
    @ValueOfEnum(enumClass = DocumentType.class)
    private String documentType;
    @NotBlank(message = "Document-number is required.")
    private String documentNumber;
    @NotNull(message = "Expiration-date cannot be null.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    private Boolean hasHearingDifficulties = Boolean.FALSE;
    private Boolean hasVisionDifficulties = Boolean.FALSE;
    private Boolean requiredWheelchair = Boolean.FALSE;
    @NotNull(message = "Tariff-id cannot be null.")
    @Min(1)
    private Long tariffId;
}
