package com.example.aviasales.exception.not_found;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String email) {
        super("user", "email", email);
    }
}
