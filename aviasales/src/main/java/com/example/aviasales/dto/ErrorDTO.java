package com.example.aviasales.dto;

import com.example.aviasales.util.enums.Code;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Schema(description = "Сущность сообщения об ошибке")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    @Schema(description = "Код ошибки", example = "RESOURCE_NOT_FOUND")
    private Code code;
    @Schema(description = "Время регистрации ошибки", example = "2023-04-08T12:39:02.929+00:00")
    private Date timestamp;
    @Schema(description = "Описание ошибки", example = "Flight with flight-id[2] not found in the system")
    private String message;
    @Schema(description = "Связанный запрос", example = "uri=/flights/2")
    private String description;
}
