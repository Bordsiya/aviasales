package com.example.mailservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
//@EnableFeignClients("com.example.mailservice")
public class MailServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MailServiceApplication.class)
                //.bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
