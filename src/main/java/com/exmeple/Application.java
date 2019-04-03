package com.exmeple;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Application {
    private static String URL = "http://localhost:8080";

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        String username;
        String chat = "";

        System.out.println("Input Username:");
        username = scanner.next();

        StompSessionHandler chatStompSessionHandler = new ChatStompSessionHandler();
        StompSession session = createWsConnection(chatStompSessionHandler);
        session.subscribe("/chat", chatStompSessionHandler);

        System.out.println("\n\nYou are logged in as '" + username + "'");
        System.out.println("Type message with format 'message:text-message'");
        System.out.println("Type 'exit' to quite from application.\n\n");

        do {
            String[] messages = chat.split(":");
            if (messages.length == 2) {
                System.out.println();
                session.send("/app/send/" + messages[0], messages[1]);
            }

            chat = scanner.nextLine();
        }
        while (!chat.equalsIgnoreCase("exit"));
    }

    private static StompSession createWsConnection(StompSessionHandler stompSessionHandler) throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(client));

        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        return stompClient.connect(URL + "/socket", new WebSocketHttpHeaders(), stompSessionHandler).get();
    }


}
