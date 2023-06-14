package com.example.aviasales.controller.websockets;

import com.example.aviasales.dto.responses.MailServiceResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class MailWebSocketController {
    // тут будет jmslistener
    // app/mail-message
    @MessageMapping("/mail-message")
    public void handleMailServiceResponse(@Payload @Valid MailServiceResponse mailServiceResponse) {
        // TODO(обновление таблицы сообщений)
    }
}
