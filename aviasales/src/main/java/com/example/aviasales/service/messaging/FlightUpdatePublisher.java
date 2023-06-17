package com.example.aviasales.service.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.aviasales.entity.Flight;
import com.example.aviasales.entity.Passenger;
import com.example.aviasales.util.FlightGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightUpdatePublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic flightsTopic;
    private final ChannelTopic passengersTopic;
    private final FlightGenerator generator;
    private final ObjectMapper mapper;

    private final List<Flight> records = new ArrayList<>();

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void produceNewFlight() {
//        var newContractor = generator.generateActive();
//        records.add(newContractor);
//        notificationService.subscribe(
//                new NotificationSubscription(
//                        1,
//                        NotificationType.CONTRACTOR_INFO_UPDATE,
//                        String.valueOf(newContractor.inn())
//                )
//        );
        publish(new Passenger(), passengersTopic);
    }

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void produceFlightUpdate() {
//        var random = ThreadLocalRandom.current();
//        var previous = records.get(random.nextInt(0, records.size()));
//        var updated = generator.generateFromExistingNonActive(previous);
//
//        if (previous.status() == ContractorStatus.ACTIVE) {
//            publish(updated);
//        } else {
//            produceNewContractor();
//        }
        publish(new Flight(), flightsTopic);
    }

    private <T> void publish(T record, ChannelTopic topic) {
        try {
            redisTemplate.convertAndSend(topic.getTopic(), mapper.writeValueAsString(record));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
