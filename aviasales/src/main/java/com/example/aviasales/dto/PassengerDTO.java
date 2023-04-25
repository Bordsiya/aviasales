package com.example.aviasales.dto;

import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.enums.Gender;
import com.example.aviasales.util.annotations.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
public class PassengerDTO {

    public PassengerDTO(
            String firstName,
            String lastName,
            String patronymic,
            String gender,
            String citizenship,
            Boolean isKid,
            String documentType,
            String documentNumber,
            LocalDate expirationDate,
            Boolean hasHearingDifficulties,
            Boolean hasVisionDifficulties,
            Boolean requiredWheelchair,
            Long tariffId
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.gender = gender;
        this.citizenship = citizenship;
        this.isKid = isKid;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.expirationDate = expirationDate;
        this.hasHearingDifficulties = (hasHearingDifficulties == null ? Boolean.FALSE : hasHearingDifficulties);
        this.hasVisionDifficulties = (hasVisionDifficulties == null ? Boolean.FALSE : hasVisionDifficulties);
        this.requiredWheelchair = (requiredWheelchair == null ? Boolean.FALSE : requiredWheelchair);
        this.tariffId = tariffId;
    }
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
    private Boolean hasHearingDifficulties;
    private Boolean hasVisionDifficulties;
    private Boolean requiredWheelchair;
    @NotNull(message = "Tariff-id cannot be null.")
    @Min(1)
    private Long tariffId;
}
