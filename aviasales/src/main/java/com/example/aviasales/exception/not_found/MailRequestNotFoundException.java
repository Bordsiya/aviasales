package com.example.aviasales.exception.not_found;

public class MailRequestNotFoundException extends ResourceNotFoundException {
    public MailRequestNotFoundException(Long id) {
        super("MailRequest", "mail-request-id", id.toString());
    }
}