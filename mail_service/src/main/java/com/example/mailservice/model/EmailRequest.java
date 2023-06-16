package com.example.mailservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
        private Long mailRequestId;
        private String email;
        private String subject;
        private String text;
}
