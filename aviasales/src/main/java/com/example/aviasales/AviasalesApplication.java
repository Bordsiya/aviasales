package com.example.aviasales;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@EnableFeignClients
@EnableProcessApplication
//@EnableRabbit
public class AviasalesApplication {
    public static void main(String[] args) {
        SpringApplication.run(AviasalesApplication.class, args);
    }
}
