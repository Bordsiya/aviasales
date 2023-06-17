package com.example.aviasales.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import com.example.aviasales.dto.requests.MailServiceRequest;
import com.example.aviasales.dto.responses.MailServiceResponse;
import com.example.aviasales.exception.not_found.MailRequestNotFoundException;
import com.example.aviasales.repo.MailRequestRepository;
import com.example.aviasales.util.enums.MailRequestStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Controller
@RequiredArgsConstructor
public class StompController implements StompSessionHandler {
    private static final Logger log = LoggerFactory.getLogger(StompController.class);
    private final MailRequestRepository repository;
    @Getter
    Map<String, StompSession.Subscription> subscriptions = new HashMap<>();
    StompSession stompSession = null;

    @EventListener(value = ApplicationReadyEvent.class)
    public void connect() {
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("login", "user");
        connectHeaders.add("passcode", "password");

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient transport = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            stompClient.connect("ws://127.0.0.1:9097/websocket-server", handshakeHeaders, connectHeaders, this);
            log.info("Connected to websocket-server");
        } catch (Exception e) {
            log.error("Connection failed.");
        }
    }

    private boolean isConnected() {
        return stompSession.isConnected();
    }

    public void subscribe(String queueName) {
        if (isSubscribed(queueName)) {
            return;
        }
        log.info("Subscribing to {}", queueName);
        StompSession.Subscription subscription = stompSession.subscribe("/queue/" + queueName, this);
        subscriptions.put(queueName, subscription);
    }

    public boolean isSubscribed(String queueName) {
        return subscriptions
                .containsKey(queueName);
    }

    public void unsubscribe(String queueName) {
        subscriptions
                .get(queueName)
                .unsubscribe();
        subscriptions.remove(queueName);
    }

    public void send(String destination, MailServiceRequest mailServiceRequest) {
        if (isConnected()) {
            stompSession.send("/app/" + destination, mailServiceRequest);
        } else {
            log.error("Not connected, can't send mail request");
        }
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        stompSession = session;
        log.info("Connection to STOMP server established.\n" +
                "Session: {}\n" +
                "Headers: {}", session, connectedHeaders);
        subscribe("mail-messages");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        log.info("Got an exception while handling a frame.\n" +
                "Command: {}\n" +
                "Headers: {}\n" +
                "Payload: {}\n" +
                "{}", command, headers, payload, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Retrieved a transport error: {}", session);
        exception.printStackTrace();
        if (!isConnected()) {
            subscriptions.clear();
            connect();
        }
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return MailServiceResponse.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        MailServiceResponse mailServiceResponse = ((MailServiceResponse) payload);

        var requestId = mailServiceResponse.getMailRequestId();
        log.info("Got mail ack: {}", requestId);

        var found = repository.findById(requestId);
        found.orElseThrow(() -> new MailRequestNotFoundException(mailServiceResponse.getMailRequestId()));
        var maiLRequest = found.get();

        maiLRequest.setStatus(MailRequestStatus.RECEIVED);
        repository.save(maiLRequest);
        log.info("Mark mail request {} as received", requestId);
    }

    @PreDestroy
    void onShutDown() {
        for (String key : subscriptions.keySet()) {
            unsubscribe(key);
        }

        if (stompSession != null) {
            stompSession.disconnect();
        }
    }
}