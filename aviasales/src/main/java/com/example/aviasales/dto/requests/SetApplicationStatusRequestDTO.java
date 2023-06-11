package com.example.aviasales.dto.requests;

import com.example.aviasales.util.annotations.ValueOfEnum;
import com.example.aviasales.util.enums.ApplicationStatus;
import com.example.aviasales.util.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность изменения заявки")
public class SetApplicationStatusRequestDTO {
    @NotNull(message = "Application-id cannot be null.")
    @Min(1)
    @JsonView
    private Long applicationId;

    @NotBlank(message = "Application-status is required.")
    @ValueOfEnum(enumClass = ApplicationStatus.class)
    @JsonView
    private String applicationStatus;

    @JsonView
    private String comment;
}
