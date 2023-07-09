package com.adnancrnovrsanin.ping.chat.DTOs;

import com.adnancrnovrsanin.ping.chat.ChatType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private String id;
    private String chatName;
    private String chatImageUrl;
    private String chatDescription;
    @Enumerated(EnumType.STRING)
    private ChatType chatType;
    private String creatorPhoneNumber;
    private List<String> memberPhoneNumbers;
    private String latestMessage;
    private String createdAt;
    private String updatedAt;
}
