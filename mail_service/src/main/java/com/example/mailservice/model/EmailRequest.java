package com.example.mailservice.model;

public record EmailRequest(
        Long mailRequestId,
        String email,
        String subject,
        String text
) {
}
