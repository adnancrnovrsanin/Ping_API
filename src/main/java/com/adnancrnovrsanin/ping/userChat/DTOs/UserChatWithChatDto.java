package com.adnancrnovrsanin.ping.userChat.DTOs;

import com.adnancrnovrsanin.ping.chat.DTOs.ChatDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChatWithChatDto {
    private Long id;
    private ChatDto chat;
    private String userPhoneNumber;
    private boolean isAdmin;
    private boolean isCreator;
    private boolean didUpdateLast;
}
