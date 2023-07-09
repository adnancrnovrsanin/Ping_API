package com.adnancrnovrsanin.ping.message.DTOs;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String id;
    private String message;
    private String mediaUrl;
    private String messageType;
    private String senderPhoneNumber;
    private String replyToMessageId;
    private String chatId;
    private String createdAt;
}
