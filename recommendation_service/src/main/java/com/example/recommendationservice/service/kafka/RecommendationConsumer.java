package com.example.recommendationservice.service.kafka;

import com.example.recommendationservice.config.KafkaConfig;
import com.example.recommendationservice.service.event.BuyTicketEvent;
import com.example.recommendationservice.service.event.BuyTicketEventWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RecommendationConsumer {
    Logger log = LoggerFactory.getLogger(RecommendationConsumer.class);
    private final BuyTicketEventWorker worker;
    private ObjectMapper mapper;

    @Autowired
    public RecommendationConsumer(
            BuyTicketEventWorker buyTicketEventWorker,
            ObjectMapper mapper
    ) {
        this.mapper = mapper;
        this.worker = buyTicketEventWorker;
    }

    @KafkaListener(topics = "recommendations_topic", groupId = "group-id")
    public void consumeMessage(String message) throws JsonProcessingException {
        BuyTicketEvent buyTicketEvent = mapper.readValue(message, BuyTicketEvent.class);
        log.info("Got buy ticket event: " + buyTicketEvent.userId());
        worker.process(buyTicketEvent);
    }
}
