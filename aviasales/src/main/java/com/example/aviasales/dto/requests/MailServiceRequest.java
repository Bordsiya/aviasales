package com.example.aviasales.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailServiceRequest {
    private Long mailRequestId;
    private String email;
    private String subject;
    private String text;
}
