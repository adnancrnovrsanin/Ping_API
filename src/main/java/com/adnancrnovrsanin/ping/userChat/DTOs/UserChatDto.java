package com.adnancrnovrsanin.ping.userChat.DTOs;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChatDto {
    private Long id;
    private String chatId;
    private String userPhoneNumber;
    private boolean admin;
    private boolean creator;
    private boolean updateLast;
}
