package com.example.aviasales.service.scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.example.aviasales.entity.MailRequest;
import com.example.aviasales.repo.MailRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledMailReSender {
    private final MailRequestRepository repository;
    private static final int DELAY_MINUTES = 10;
    private static final int JITTER_MINUTES = 1;

    @Scheduled(fixedDelay = DELAY_MINUTES, timeUnit = TimeUnit.MINUTES)
    public void checkAndResendIfNecessary() {
        var from = Instant.now()
                .minusSeconds(Duration.ofMinutes(DELAY_MINUTES + JITTER_MINUTES).toSeconds());

        var notSuccess = repository.findAllByCreatedAtGreaterThan(from)
                .stream()
                .filter(it -> !it.getSuccess())
                .toList();

        for (MailRequest mr : notSuccess) {
            try {
                // TODO
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
