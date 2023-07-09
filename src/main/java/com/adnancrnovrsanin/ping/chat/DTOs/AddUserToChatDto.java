package com.adnancrnovrsanin.ping.chat.DTOs;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserToChatDto {
    private String phoneNumber;
    private String chatId;
}
