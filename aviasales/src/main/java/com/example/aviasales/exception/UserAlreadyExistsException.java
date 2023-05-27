package com.example.aviasales.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User with email [" + email + "] is already exists");
    }
}
