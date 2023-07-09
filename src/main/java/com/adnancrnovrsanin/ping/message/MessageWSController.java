package com.adnancrnovrsanin.ping.message;

import com.adnancrnovrsanin.ping.message.DTOs.MessageDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.Date;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageWSController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }

    @Transactional
    @MessageMapping("/chat")
    public MessageDto receiveMessage(@Payload MessageDto message) {
        message.setId(UUID.randomUUID().toString());
        message.setCreatedAt((new Date()).toInstant().toString());
        simpMessagingTemplate.convertAndSendToUser(message.getChatId().toString(), "/message", message);
        var newMessage = messageService.createMessage(message);
        if (newMessage == null) return null;
        return newMessage;
    }
}
