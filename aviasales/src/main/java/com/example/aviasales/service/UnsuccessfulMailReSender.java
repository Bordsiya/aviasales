package com.example.aviasales.service;

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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnsuccessfulMailReSender {
    private static final Logger log = LoggerFactory.getLogger(UnsuccessfulMailReSender.class);
    private static final int DELAY_SECONDS = 1;
    private final MailRequestRepository repository;
    private final StompController stompController;
    private final ObjectMapper objectMapper;

    public ReSendMailsResult checkAndResendIfNecessary() {
        var now = Instant.now();
        var lastJobTime = now.minusSeconds(DELAY_SECONDS);
        var found = repository.findAllByCreatedAtGreaterThan(lastJobTime.minusSeconds(DELAY_SECONDS));

        // Mark as FAILED for expired jobs
        var reCentJobs = found.stream()
                .filter(it -> it.getStatus() == MailRequestStatus.RE_SENT)
                .filter(it -> it.getCreatedAt().isBefore(lastJobTime))
                .toList();
        for (MailRequest mr : reCentJobs) {
            log.info(mr.getPayload());
            mr.setStatus(MailRequestStatus.FAILED);
            repository.save(mr);
        }

        // Mark as RE_SENT for jobs should be re-sent
        var notSuccessAtLastJobs = found.stream()
                .filter(it -> it.getStatus() == MailRequestStatus.SENT)
                .filter(it -> !it.getCreatedAt().isBefore(lastJobTime))
                .toList();

        int successCnt = 0;
        for (MailRequest mr : notSuccessAtLastJobs) {
            try {
                log.info(mr.getPayload());
                var mailServiceRequest = objectMapper.readValue(mr.getPayload(), MailServiceRequest.class);
                stompController.send("process-mail-message", mailServiceRequest);
                mr.setStatus(MailRequestStatus.RE_SENT);
                repository.save(mr);
                successCnt++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ReSendMailsResult(notSuccessAtLastJobs.size(), successCnt);
    }
}
