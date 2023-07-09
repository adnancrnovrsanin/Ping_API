package com.adnancrnovrsanin.ping.userChat;

import com.adnancrnovrsanin.ping.chat.ChatRepository;
import com.adnancrnovrsanin.ping.chat.DTOs.ChatDto;
import com.adnancrnovrsanin.ping.user.User;
import com.adnancrnovrsanin.ping.user.UserRepository;
import com.adnancrnovrsanin.ping.userChat.DTOs.UserChatDto;
import com.adnancrnovrsanin.ping.userChat.DTOs.UserChatWithChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChatService {
    private final UserChatRepository userChatRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public UserChatDto createUserChat(String phoneNumber, String chatId) {
        var user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
        var chat = chatRepository.findById(chatId).orElse(null);
        if (user == null || chat == null) return null;

        UserChat newUserChat = UserChat.builder()
                .user(user)
                .chat(chat)
                .isAdmin(false).isCreator(false).didUpdateLast(false)
                .build();
        newUserChat = userChatRepository.save(newUserChat);
        if (newUserChat == null) return null;

        user.addUserChat(newUserChat);
        chat.addUser(newUserChat);

        user = userRepository.save(user);
        chat = chatRepository.save(chat);
        if (user == null || chat == null) return null;

        return UserChatDto.builder()
                .id(newUserChat.getId())
                .admin(newUserChat.isAdmin())
                .creator(newUserChat.isCreator())
                .updateLast(newUserChat.isDidUpdateLast())
                .chatId(newUserChat.getChat().getId())
                .userPhoneNumber(newUserChat.getUser().getPhoneNumber())
                .build();
    }

    public List<UserChatWithChatDto> getUserChats(String phoneNumber) {
        var userChats = userChatRepository.findAllByUser_PhoneNumber(phoneNumber);
        if (userChats == null) return null;
        List<UserChatWithChatDto> chats = new ArrayList<>();
        for (UserChat userChat : userChats) {
            List<String> chatMembersPhoneNumbers = new ArrayList<>();
            chatMembersPhoneNumbers.clear();
            for (UserChat userChat1 : userChat.getChat().getUsers()) {
                if (!userChat1.getUser().getPhoneNumber().equals(phoneNumber) && !chatMembersPhoneNumbers.contains(userChat1.getUser().getPhoneNumber())) {
                    chatMembersPhoneNumbers.add(userChat1.getUser().getPhoneNumber());
                }
            }
            chats.add(
                    UserChatWithChatDto.builder()
                            .id(userChat.getId())
                            .isAdmin(userChat.isAdmin())
                            .isCreator(userChat.isCreator())
                            .didUpdateLast(userChat.isDidUpdateLast())
                            .chat(
                                    ChatDto.builder()
                                            .id(userChat.getChat().getId())
                                            .chatName(userChat.getChat().getChatName())
                                            .chatImageUrl(userChat.getChat().getChatImageUrl())
                                            .chatDescription(userChat.getChat().getChatDescription())
                                            .chatType(userChat.getChat().getChatType())
                                            .creatorPhoneNumber(userChat.getChat().getUsers().stream().filter(u -> u.isCreator()).findFirst().orElse(null).getUser().getPhoneNumber())
                                            .memberPhoneNumbers(chatMembersPhoneNumbers)
                                            .latestMessage(userChat.getChat().getLatestMessage() == null ? null : userChat.getChat().getLatestMessage())
                                            .createdAt(userChat.getChat().getCreatedAt() == null ? null : userChat.getChat().getCreatedAt().toInstant().toString())
                                            .updatedAt(userChat.getChat().getUpdatedAt() == null ? null : userChat.getChat().getUpdatedAt().toInstant().toString())
                                            .build()
                            )
                            .userPhoneNumber(userChat.getUser().getPhoneNumber())
                            .build()
            );
        }
        return chats;
    }

    public void deleteAllUserChats(String phoneNumber) {
        var userChats = userChatRepository.findAllByUser_PhoneNumber(phoneNumber);
        if (userChats == null) return;
        for (UserChat userChat : userChats) {
            userChatRepository.delete(userChat);
        }
    }

    public void removeUserFromChat(String phoneNumber, String chatId) throws ChangeSetPersister.NotFoundException {
        var userChat = userChatRepository.findByUser_PhoneNumberAndChat_Id(phoneNumber, chatId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        if (userChat == null) return;
        userChatRepository.delete(userChat);
    }
}
