package com.example.recommendationservice.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KafkaConfig {
    private final String topicName = "recommendations_topic";
}
