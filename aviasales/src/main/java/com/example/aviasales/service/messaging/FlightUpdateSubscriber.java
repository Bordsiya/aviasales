package com.example.aviasales.service.messaging;

import java.io.IOException;

import com.example.aviasales.entity.Flight;
import com.example.aviasales.service.PassengerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightUpdateSubscriber implements MessageListener {
    Logger log = LoggerFactory.getLogger(PassengerService.class);
    private final ObjectMapper mapper;

    public void onMessage(final Message message, final byte[] pattern) {
        log.info("Flight received: " + new String(message.getBody()));
        try {
            var flight = mapper.readValue(message.getBody(), Flight.class);
            // flightUpdateWorker.processFlightAsync(flight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
