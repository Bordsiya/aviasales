package com.example.mailservice.model;

public record EmailRequest(
        String receiverEmail,
        String subject,
        String text
) {
}
