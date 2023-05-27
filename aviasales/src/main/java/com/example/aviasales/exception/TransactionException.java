package com.example.aviasales.exception;

public class TransactionException extends RuntimeException {
    public TransactionException(String jobType) {
        super("Transactional exception while: " + jobType);
    }
}
