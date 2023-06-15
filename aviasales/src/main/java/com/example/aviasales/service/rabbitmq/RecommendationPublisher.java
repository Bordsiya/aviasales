package com.example.aviasales.service.rabbitmq;

import com.example.aviasales.dto.BuyTicketEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendationPublisher {
    private final AmqpTemplate template;

    private final Queue queue;

    private final ObjectMapper mapper;

    public void produceMsg(BuyTicketEvent request) {
        try {
            var toSend = mapper.writeValueAsString(request);
            template.convertAndSend(queue.getName(), toSend);
            System.out.println("Sent mail request: " + toSend);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
