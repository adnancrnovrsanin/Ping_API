package com.adnancrnovrsanin.ping.chat;

import com.adnancrnovrsanin.ping.chat.DTOs.AddUserToChatDto;
import com.adnancrnovrsanin.ping.chat.DTOs.ChatDto;
import com.adnancrnovrsanin.ping.chat.DTOs.NewChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatRepository chatRepository;


    @PostMapping
    public ResponseEntity<NewChatDto> createChat(@RequestBody ChatDto chatDto) {
        var chat = chatService.createChat(chatDto);

        if (chat == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(chat);
    }

    @PostMapping("/add")
    public ResponseEntity addUserToChat(@RequestBody AddUserToChatDto request) {
        var chat = chatService.addUserToChat(request.getPhoneNumber(), request.getChatId());

        if (chat == false) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updateChat(@RequestBody ChatDto chatDto) {
        var chat = chatService.updateChat(chatDto);

        if (chat == false) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity deleteChat(@PathVariable String chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity deleteAllChats() {
        chatRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
}
