package com.example.aviasales.service;

import java.time.Duration;
import java.time.Instant;

import com.example.aviasales.dto.ReSendMailsResult;
import com.example.aviasales.entity.MailRequest;
import com.example.aviasales.repo.MailRequestRepository;
import com.example.aviasales.util.enums.MailRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnsuccessfulMailReSender {
    private final MailRequestRepository repository;
    private static final int DELAY_MINUTES = 10;
    private static final int JITTER_MINUTES = 1;

    public ReSendMailsResult checkAndResendIfNecessary() {
        var from = Instant.now()
                .minusSeconds(Duration.ofMinutes(DELAY_MINUTES + JITTER_MINUTES).toSeconds());

        var notSuccess = repository.findAllByCreatedAtGreaterThan(from)
                .stream()
                .filter(it -> it.getStatus() == MailRequestStatus.SENT)
                .toList();


        int successCnt = 0;
        for (MailRequest mr : notSuccess) {
            try {
                // TODO send to rabbitMQ

                mr.setStatus(MailRequestStatus.RE_SENT);
                repository.save(mr);
                successCnt++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ReSendMailsResult(notSuccess.size(), successCnt);
    }
}
