package com.example.mailservice.controlers;

import com.example.mailservice.exception.MailException;
import com.example.mailservice.model.EmailRequest;
import com.example.mailservice.model.EmailResponse;
import com.example.mailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private final EmailService emailService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${messaging.email-response-queue}")
    private String responseQueue;

    @MessageMapping("/process-mail-message")
    public void processMessage(EmailRequest emailRequest) {
        try {
            log.info("Got a message to send email: {}", emailRequest);
            emailService.send(emailRequest);

            var requestId = emailRequest.getMailRequestId();
            log.info("Email successfully sent, sending ack: {}", requestId);
            simpMessagingTemplate.convertAndSend(responseQueue, new EmailResponse(requestId));
        } catch (MailException ignored) {
            log.info("Email data is incorrect: {}", emailRequest);
        }
    }
}
