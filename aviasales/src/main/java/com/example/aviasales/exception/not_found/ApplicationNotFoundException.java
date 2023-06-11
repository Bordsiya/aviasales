package com.example.aviasales.exception.not_found;

public class ApplicationNotFoundException extends ResourceNotFoundException {
    public ApplicationNotFoundException(Long applicationId) {
        super("Application", "application-id", applicationId.toString());
    }
}
