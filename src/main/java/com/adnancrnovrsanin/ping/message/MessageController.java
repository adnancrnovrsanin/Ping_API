package com.adnancrnovrsanin.ping.message;

import com.adnancrnovrsanin.ping.chat.ChatRepository;
import com.adnancrnovrsanin.ping.chat.ChatService;
import com.adnancrnovrsanin.ping.message.DTOs.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto message) {
        var newMessage = messageService.createMessage(message);
        if (newMessage == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newMessage);
    }

    @GetMapping("{chatId}")
    public ResponseEntity<List<MessageDto>> getAllMessagesForChat(@PathVariable String chatId) {
        var chat = chatRepository.findById(chatId).orElse(null);
        var messages = messageService.getAllMessagesForChat(chatId);
        if (messages == null || chat == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("{messageId}/message")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable String messageId) {
        var message = messageRepository.findById(messageId.toString()).orElse(null);
        if (message == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(
                MessageDto.builder()
                        .id(message.getId())
                        .chatId(message.getChat().getId())
                        .senderPhoneNumber(message.getSender().getPhoneNumber())
                        .message(message.getMessage())
                        .replyToMessageId(message.getReplyTo() == null ? null : message.getReplyTo().getId())
                        .build()
        );
    }

    @DeleteMapping("{messageId}")
    public ResponseEntity deleteMessage(@PathVariable Long messageId) {
        var message = messageRepository.findById(messageId.toString()).orElse(null);
        if (message == null) return ResponseEntity.notFound().build();
        messageRepository.delete(message);
        return ResponseEntity.ok().build();
    }
}
