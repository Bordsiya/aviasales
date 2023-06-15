//package com.example.mailservice.rabbit;
//
//import com.example.mailservice.model.EmailRequest;
//import com.example.mailservice.service.EmailService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RabbitListener(queues = "mailRequestQueue")
//@RequiredArgsConstructor
//public class Subscriber {
//    private final EmailService service;
//    private final ObjectMapper mapper;
//
//    @RabbitHandler
//    public void receivedMessage(String msg) throws JsonProcessingException {
//        System.out.println("Got mail request: " + msg);
//        EmailRequest request = mapper.readValue(msg, EmailRequest.class);
//        service.send(request);
//    }
//}
