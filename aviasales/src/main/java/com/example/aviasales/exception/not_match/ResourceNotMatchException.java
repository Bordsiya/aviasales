package com.example.aviasales.exception.not_match;

public class ResourceNotMatchException extends RuntimeException {
    public ResourceNotMatchException(String entity1, String entity2) {
        super(entity1 + " not match " + entity2);
    }
}
