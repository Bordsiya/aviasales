package com.example.recommendationservice.kafka;

import com.example.recommendationservice.model.BuyTicketEvent;
import com.example.recommendationservice.service.BuyTicketEventWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationConsumer {
    private final static Logger log = LoggerFactory.getLogger(RecommendationConsumer.class);
    private final BuyTicketEventWorker worker;
    private ObjectMapper mapper;

    @KafkaListener(topics = "recommendations_topic", groupId = "group-id")
    public void consumeMessage(String message) throws JsonProcessingException {
        BuyTicketEvent buyTicketEvent = mapper.readValue(message, BuyTicketEvent.class);
        log.info("Got buy ticket event: " + buyTicketEvent.userId());
        worker.process(buyTicketEvent);
    }
}
