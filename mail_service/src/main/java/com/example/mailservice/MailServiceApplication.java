package com.example.mailservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MailServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MailServiceApplication.class).run(args);
    }
}
