package com.example.aviasales.dto;

import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.enums.Gender;
import com.example.aviasales.util.annotations.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Сущность пассажира рейса")
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
    @Schema(description = "Имя", example = "Анастасия")
    private String firstName;
    @NotBlank(message = "Last-name is required.")
    @Schema(description = "Фамилия", example = "Бордун")
    private String lastName;
    @NotBlank(message = "Patronymic is required.")
    @Schema(description = "Отчество", example = "Владимировна")
    private String patronymic;
    @NotBlank(message = "Gender is required.")
    @ValueOfEnum(enumClass = Gender.class)
    @Schema(description = "Пол", example = "FEMALE")
    private String gender;
    @NotBlank(message = "Citizenship is required.")
    @Schema(description = "Гражданство", example = "Россия")
    private String citizenship;
    @NotNull(message = "Is-kid cannot be null.")
    @Schema(description = "Является ли ребёнком?", example = "false")
    private Boolean isKid;
    @NotBlank(message = "Document-type is required.")
    @ValueOfEnum(enumClass = DocumentType.class)
    @Schema(description = "Тип документа", example = "PASSPORT")
    private String documentType;
    @NotBlank(message = "Document-number is required.")
    @Schema(description = "Номер документа", example = "2816625832")
    private String documentNumber;
    @NotNull(message = "Expiration-date cannot be null.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Дата истечения срока документа", example = "2023-06-21")
    private LocalDate expirationDate;
    @Schema(description = "Имеются ли проблемы со слухом?", example = "false")
    private Boolean hasHearingDifficulties;
    @Schema(description = "Имеются ли проблемы со зрением?", example = "false")
    private Boolean hasVisionDifficulties;
    @Schema(description = "Нужна ли специальная коляска?", example = "false")
    private Boolean requiredWheelchair;
    @NotNull(message = "Tariff-id cannot be null.")
    @Min(1)
    @Schema(description = "ID тарифа", example = "1")
    private Long tariffId;
}
