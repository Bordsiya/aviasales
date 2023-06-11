package com.example.aviasales.service;

import com.google.gson.Gson;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Map;

@Component
public class Listener {

    @JmsListener(destination = "/topic/applications-general")
    public String receiveMessage(final Message jsonMessage) throws JMSException {
        System.out.println(jsonMessage);
        return null;
    }


}
