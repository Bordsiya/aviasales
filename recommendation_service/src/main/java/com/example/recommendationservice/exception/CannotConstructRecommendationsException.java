package com.example.recommendationservice.exception;

public class CannotConstructRecommendationsException extends RuntimeException {
    public CannotConstructRecommendationsException() {
        super("Too few information to construct recommendations");
    }
}
