package com.adnancrnovrsanin.ping.user;

import com.adnancrnovrsanin.ping.user.DTOs.UserDto;
import com.adnancrnovrsanin.ping.user.DTOs.UserExistsResponse;
import com.adnancrnovrsanin.ping.userChat.DTOs.UserChatDto;
import com.adnancrnovrsanin.ping.userChat.DTOs.UserChatWithChatDto;
import com.adnancrnovrsanin.ping.userChat.UserChat;
import com.adnancrnovrsanin.ping.userChat.UserChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final CustomUserDetailsService userDetailsService;
    private final UserChatService userChatService;

    @GetMapping
    public ResponseEntity<UserDto> getCurrentUser() {
        var user = userDetailsService.getCurrentUserDto();

        if (user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<UserDto> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        var user = userDetailsService.getUserByPhoneNumberDto(phoneNumber);

        if (user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{phoneNumber}/exists")
    public ResponseEntity<UserExistsResponse> getUserByPhoneNumberIfExists(@PathVariable String phoneNumber) {
        var user = userDetailsService.getUserByPhoneNumberDto(phoneNumber);

        if (user == null) return ResponseEntity.ok(
                UserExistsResponse.builder()
                        .userExists(false)
                        .user(null)
                        .build()
        );

        return ResponseEntity.ok(
                UserExistsResponse.builder()
                        .userExists(true)
                        .user(user)
                        .build()
        );
    }

    @PostMapping("/contacts")
    public ResponseEntity<List<UserDto>> getUsersByPhoneNumbersIfExists(@RequestBody List<String> phoneNumbers) {
        var users = userDetailsService.getUsersByPhoneNumbersDto(phoneNumbers);
        return ResponseEntity.ok(users);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        var user = userDetailsService.updateUser(userDto);

        if (user == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(user);
    }

    @GetMapping("{phoneNumber}/chats")
    public ResponseEntity<List<UserChatWithChatDto>> getUserChats(@PathVariable String phoneNumber) {
        var userChats = userChatService.getUserChats(phoneNumber);

        if (userChats == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(userChats);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        var users = userDetailsService.getAllUsersDto();

        if (users == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/remove/{phoneNumber}/from/{chatId}")
    public ResponseEntity removeUserFromChat(@PathVariable String phoneNumber, @PathVariable String chatId) {
        try {
            userChatService.removeUserFromChat(phoneNumber, chatId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{phoneNumber}/allchats")
    public ResponseEntity<List<UserChatWithChatDto>> deleteUserChats(@PathVariable String phoneNumber) {
        try {
            userChatService.deleteAllUserChats(phoneNumber);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
