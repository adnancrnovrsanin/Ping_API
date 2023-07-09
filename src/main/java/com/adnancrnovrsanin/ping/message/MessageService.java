package com.adnancrnovrsanin.ping.message;

import com.adnancrnovrsanin.ping.chat.ChatRepository;
import com.adnancrnovrsanin.ping.message.DTOs.MessageDto;
import com.adnancrnovrsanin.ping.user.CustomUserDetailsService;
import com.adnancrnovrsanin.ping.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.adnancrnovrsanin.ping.message.MessageType.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto createMessage(MessageDto message) {
        var chat = chatRepository.findById(message.getChatId()).orElse(null);
        var sender = userDetailsService.getUserByPhoneNumber(message.getSenderPhoneNumber());
        if (chat == null || sender == null) return null;
        var messagesToSave = new ArrayList<Message>();

        MessageType messageType;
        if (message.getMessageType().equals("IMAGE")) messageType = IMAGE;
        else if (message.getMessageType().equals("VIDEO")) messageType = VIDEO;
        else if (message.getMessageType().equals("AUDIO")) messageType = AUDIO;
        else if (message.getMessageType().equals("INFO")) messageType = INFO;
        else messageType = TEXT;

        Message newMessage = Message.builder()
                .id(message.getId() == null ? UUID.randomUUID().toString() : message.getId())
                .chat(chat)
                .message(message.getMessage())
                .mediaUrl(message.getMediaUrl())
                .messageType(messageType)
                .createdAt(message.getCreatedAt() != null ? Date.from(Instant.parse(message.getCreatedAt())) : new Date())
                .sender(sender)
                .build();
        messagesToSave.add(newMessage);
        if (message.getReplyToMessageId() != null) {
            var replyTo = messageRepository.findById(message.getReplyToMessageId()).orElse(null);
            if (replyTo == null) return null;
            newMessage.setReplyTo(replyTo);

            replyTo.addReply(newMessage);
            messagesToSave.add(replyTo);
        } else newMessage.setReplyTo(null);

        newMessage = messageRepository.save(newMessage);

        chat.addMessage(newMessage);

        sender.addMessage(newMessage);

        if (newMessage == null) return null;

        return MessageDto.builder()
                .id(newMessage.getId())
                .chatId(newMessage.getChat().getId())
                .message(newMessage.getMessage())
                .mediaUrl(newMessage.getMediaUrl())
                .messageType(newMessage.getMessageType().toString())
                .createdAt(newMessage.getCreatedAt().toInstant().toString())
                .senderPhoneNumber(newMessage.getSender().getPhoneNumber())
                .replyToMessageId(newMessage.getReplyTo() == null ? null : newMessage.getReplyTo().getId())
                .build();
    }

    public boolean deleteMessage(Long messageId) {
        var message = messageRepository.findById(messageId.toString()).orElse(null);
        if (message == null) return false;
        messageRepository.delete(message);
        return true;
    }

    public List<MessageDto> getAllMessagesForChat(String chatId) {
        var chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) return null;
        var chatMessages = chat.getMessages();
        List<MessageDto> output = new ArrayList<>();
        if (chatMessages != null) {
            for (var message : chatMessages) {
                output.add(
                        MessageDto.builder()
                                .id(message.getId())
                                .chatId(message.getChat().getId())
                                .senderPhoneNumber(message.getSender().getPhoneNumber())
                                .message(message.getMessage())
                                .mediaUrl(message.getMediaUrl())
                                .messageType(message.getMessageType().toString())
                                .createdAt(message.getCreatedAt().toInstant().toString())
                                .replyToMessageId(message.getReplyTo() == null ? null : message.getReplyTo().getId())
                                .build()
                );
            }
        }
        return output;
    }
}
