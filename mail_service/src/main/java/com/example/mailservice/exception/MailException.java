package com.example.mailservice.exception;

public class MailException extends RuntimeException {
    public MailException(String receiverMail) {
        super("Error occurred while sending email to address [" + receiverMail + "]");
    }
}
