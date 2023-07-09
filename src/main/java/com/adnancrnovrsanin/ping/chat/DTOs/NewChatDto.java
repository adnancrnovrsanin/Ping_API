package com.adnancrnovrsanin.ping.chat.DTOs;

public record NewChatDto (
        String id,
        String chatName,
        String chatDescription,
        String chatType,
        String chatImageUrl,
        String createdAt,
        String updatedAt
){
}
