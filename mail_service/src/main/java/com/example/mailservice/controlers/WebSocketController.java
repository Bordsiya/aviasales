package com.example.mailservice.controlers;

import com.example.demo1.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    Logger log = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/process-message")
    @SendTo("/queue/messages")
    public MessageDto processMessage(MessageDto messageDto) {
        return new MessageDto("Hello " + messageDto.getMessage());
    }

}
