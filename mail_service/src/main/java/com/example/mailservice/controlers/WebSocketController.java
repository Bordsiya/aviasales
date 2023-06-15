package com.example.mailservice.controlers;

import com.example.mailservice.exception.MailException;
import com.example.mailservice.model.EmailRequest;
import com.example.mailservice.model.EmailResponse;
import com.example.mailservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private EmailService emailService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(
            EmailService emailService,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.emailService = emailService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/process-mail-message")
    public void processMessage(EmailRequest emailRequest) {
        try {
            emailService.send(emailRequest);
        }
        catch (MailException mailException) {
            return;
        }
        simpMessagingTemplate.convertAndSend(
                "/queue/mail-messages",
                new EmailResponse(emailRequest.mailRequestId())
        );
    }

}
