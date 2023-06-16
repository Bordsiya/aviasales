package com.example.aviasales.service.kafka;

import com.example.aviasales.config.KafkaConfig;
import com.example.aviasales.dto.BuyTicketEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecommendationProducer {
    private KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;
    private ObjectMapper mapper;

    @Autowired
    public RecommendationProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            KafkaConfig kafkaConfig,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = kafkaConfig;
        this.mapper = objectMapper;
    }

    public void sendMessage(BuyTicketEvent buyTicketEvent) throws JsonProcessingException {
        kafkaTemplate.send(kafkaConfig.getTopicName(), mapper.writeValueAsString(buyTicketEvent));
    }
}
