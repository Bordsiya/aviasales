package com.example.aviasales.dto.responses;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailServiceResponse {
    @NotNull(message = "Mail-id cannot be null.")
    @Min(1)
    @JsonView
    private Long mailId;
}
