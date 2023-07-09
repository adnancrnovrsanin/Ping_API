package com.adnancrnovrsanin.ping.chat;

import com.adnancrnovrsanin.ping.chat.DTOs.ChatDto;
import com.adnancrnovrsanin.ping.chat.DTOs.NewChatDto;
import com.adnancrnovrsanin.ping.message.Message;
import com.adnancrnovrsanin.ping.user.CustomUserDetailsService;
import com.adnancrnovrsanin.ping.user.User;
import com.adnancrnovrsanin.ping.user.UserRepository;
import com.adnancrnovrsanin.ping.userChat.UserChat;
import com.adnancrnovrsanin.ping.userChat.UserChatRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public NewChatDto createChat(ChatDto chat) {
        var creatorUser = userDetailsService.getUserByPhoneNumber(chat.getCreatorPhoneNumber());
        List<String> memberPhoneNumbers = chat.getMemberPhoneNumbers();
        var memberUsers = userDetailsService.getUsersByPhoneNumber(memberPhoneNumbers);

        if (creatorUser == null || memberUsers == null) return null;

        Chat newChat = Chat.builder()
                .chatName(chat.getChatType() == ChatType.GROUPCHAT ? chat.getChatName() : null)
                .chatDescription(chat.getChatType() == ChatType.GROUPCHAT ? chat.getChatDescription() : null)
                .chatType(chat.getChatType())
                .chatImageUrl(chat.getChatType() == ChatType.GROUPCHAT ? chat.getChatImageUrl() : null)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        newChat = chatRepository.save(newChat);

        if (newChat == null) return null;

        UserChat creator = UserChat.builder()
                .chat(newChat)
                .user(creatorUser)
                .isAdmin(true).isCreator(true).didUpdateLast(true)
                .build();

        creator = userChatRepository.save(creator);

        if (creator == null) return null;

        newChat.addUser(creator);

        for (User memberUser : memberUsers) {
            if (memberUser.getPhoneNumber() == creatorUser.getPhoneNumber()) continue;

            UserChat member = UserChat.builder()
                    .chat(newChat)
                    .user(memberUser)
                    .isAdmin(false).isCreator(false).didUpdateLast(false)
                    .build();

            member = userChatRepository.save(member);

            if (member == null) return null;

            newChat.addUser(member);

            memberUser.addUserChat(member);

            memberUser = userRepository.save(memberUser);
            if (memberUser == null) return null;
        }

        newChat = chatRepository.save(newChat);

        if (newChat == null) return null;

        creatorUser.addUserChat(creator);
        creatorUser = userRepository.save(creatorUser);
        if (creatorUser == null) return null;

        return new NewChatDto(
                newChat.getId(),
                newChat.getChatName(),
                newChat.getChatDescription(),
                newChat.getChatType().toString(),
                newChat.getChatImageUrl(),
                newChat.getCreatedAt().toInstant().toString(),
                newChat.getUpdatedAt().toInstant().toString()
        );
    }

    public boolean addUserToChat(String phoneNumber, String chatId) {
        var chat = chatRepository.findById(chatId).orElse(null);
        var user = userDetailsService.getUserByPhoneNumber(phoneNumber);
        if (chat == null || user == null) return false;
        var userChat = UserChat.builder()
                .chat(chat)
                .user(user)
                .isAdmin(false).isCreator(false).didUpdateLast(false)
                .build();
        userChat = userChatRepository.save(userChat);
        if (userChat == null) return false;
        chat.addUser(userChat);
        chat = chatRepository.save(chat);
        if (chat == null) return false;
        return true;
    }

    public Boolean updateChat(ChatDto newChat) {
        var currUser = userDetailsService.getCurrentUser();
        var chat = chatRepository.findById(newChat.getId()).orElse(null);

        if (chat == null || currUser == null) return false;

        if (chat.getChatType() != ChatType.GROUPCHAT) return false;

        if (chat.getChatName() != null) chat.setChatName(newChat.getChatName());
        if (chat.getChatDescription() != null) chat.setChatDescription(newChat.getChatDescription());
        if (chat.getChatImageUrl() != null) chat.setChatImageUrl(newChat.getChatImageUrl());

        chat.setUpdatedAt(new Date());

        chat = chatRepository.save(chat);

        if (chat == null) return false;

        return true;
    }

    public void deleteChat(String chatId) {
        var chat = chatRepository.findById(chatId).orElse(null);

        if (chat == null) return;

        chatRepository.delete(chat);
    }
}
