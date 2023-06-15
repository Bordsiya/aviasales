package com.example.aviasales.config;


import javax.jms.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageType;

import java.io.IOException;

@Configuration
public class JmsConfig {
    private static final Log log = LogFactory.getLog(JmsConfig.class);

    @Bean
    public DefaultMessageListenerContainer jmsListener(ConnectionFactory connectionFactory) {

        log.info("connectionFactory => " + connectionFactory);

        DefaultMessageListenerContainer jmsListener = new DefaultMessageListenerContainer();
        jmsListener.setConnectionFactory(connectionFactory);
        jmsListener.setDestinationName("mail-responses");
        jmsListener.setPubSubDomain(false);

        MessageListenerAdapter adapter = new MessageListenerAdapter(new Receiver());
        adapter.setDefaultListenerMethod("receiveMailResponse");

        jmsListener.setMessageListener(adapter);
        final MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter() {
            @Override
            public Object fromMessage(Message message) throws JMSException, MessageConversionException {
                return message instanceof ObjectMessage ? ((ObjectMessage) message).getObject() : super.fromMessage(message);
            }

            @Override
            protected TextMessage mapToTextMessage(Object object, Session session, ObjectMapper objectMapper) throws JMSException, IOException {
                final TextMessage rv = super.mapToTextMessage(object, session, objectMapper);
                rv.setStringProperty("content-type", "application/json;charset=UTF-8");
                return rv;
            }
        };
        messageConverter.setTargetType(MessageType.TEXT);
        return jmsListener;
    }

    static class Receiver {
        public void receiveMailResponse(TextMessage message) {
            log.info("Received " + message);
            // TODO(handle info)
        }
    }
}
