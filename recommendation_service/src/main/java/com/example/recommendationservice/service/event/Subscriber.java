package com.example.recommendationservice.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "recommendationQueue")
@RequiredArgsConstructor
public class Subscriber {
    private final ObjectMapper mapper;
    private final BuyTicketEventWorker worker;

    @RabbitHandler
    public void receivedMessage(String msg) throws JsonProcessingException {
        System.out.println("Got buy ticket event: " + msg);
        BuyTicketEvent event = mapper.readValue(msg, BuyTicketEvent.class);
        worker.process(event);
    }
}
