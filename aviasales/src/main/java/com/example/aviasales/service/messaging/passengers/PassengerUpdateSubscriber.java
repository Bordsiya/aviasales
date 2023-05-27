package com.example.aviasales.service.messaging.passengers;

import java.io.IOException;

import com.example.aviasales.entity.Passenger;
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
public class PassengerUpdateSubscriber implements MessageListener {
    Logger log = LoggerFactory.getLogger(PassengerService.class);
    private final ObjectMapper mapper;

    public void onMessage(final Message message, final byte[] pattern) {
//        log.info("Passenger received: " + new String(message.getBody()));
        try {
            var passenger = mapper.readValue(message.getBody(), Passenger.class);
            // passengerUpdateWorker.processPassengerAsync(flight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
