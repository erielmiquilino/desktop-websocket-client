package com.exmeple;

import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

public class ChatStompSessionHandler implements StompSessionHandler {

    @Override
    public void afterConnected(StompSession session, StompHeaders headers) {
        session.send("/app/send/message", "ENTREI");
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object payload) {
        System.out.println("handleFrame");
        String msg = (String) payload;
        System.out.println("You've got message form '" + msg);
    }

    @Override
    public void handleException(StompSession session,
                                StompCommand command,
                                StompHeaders headers,
                                byte[] payload, Throwable exception) {
        System.out.println(exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        if (exception instanceof ConnectionLostException) {
            System.out.println("Connection Lost!");
        }
    }

}
