package com.example.aviasales.service;

import java.time.Duration;
import java.time.Instant;

import com.example.aviasales.controller.StompController;
import com.example.aviasales.dto.ReSendMailsResult;
import com.example.aviasales.dto.requests.MailServiceRequest;
import com.example.aviasales.entity.MailRequest;
import com.example.aviasales.repo.MailRequestRepository;
import com.example.aviasales.util.enums.MailRequestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnsuccessfulMailReSender {
    private final MailRequestRepository repository;
    Logger log = LoggerFactory.getLogger(UnsuccessfulMailReSender.class);
    private static final int DELAY_MINUTES = 10;
    private static final int JITTER_MINUTES = 1;

    private StompController stompController;
    private ObjectMapper objectMapper;

    @Autowired
    public UnsuccessfulMailReSender(
            StompController stompController,
            MailRequestRepository mailRequestRepository,
            ObjectMapper objectMapper
    ) {
        this.stompController = stompController;
        this.repository = mailRequestRepository;
        this.objectMapper = objectMapper;
    }

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
                log.info(mr.getPayload());
               MailServiceRequest mailServiceRequest = objectMapper.readValue(mr.getPayload(), MailServiceRequest.class);
               stompController.send(
                       "process-mail-message",
                       mailServiceRequest);

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
