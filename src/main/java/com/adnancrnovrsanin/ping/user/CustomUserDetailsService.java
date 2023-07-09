package com.adnancrnovrsanin.ping.user;

import com.adnancrnovrsanin.ping.user.DTOs.UserDto;
import com.adnancrnovrsanin.ping.user.DTOs.UserExistsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDto getUserByPhoneNumberDto(String phoneNumber) {
        var user = userRepository.findByPhoneNumber(phoneNumber)
                .orElse(null);

        if (user == null) return null;

        return UserDto.builder()
                .displayName(user.getDisplayName())
                .phoneNumber(user.getPhoneNumber())
                .about(user.getAbout())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    public UserDto createUser(UserDto user) {
        var existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if (existingUser.isPresent()) {
            return UserDto.builder()
                    .displayName(existingUser.get().getDisplayName())
                    .phoneNumber(existingUser.get().getPhoneNumber())
                    .about(existingUser.get().getAbout())
                    .profilePictureUrl(existingUser.get().getProfilePictureUrl())
                    .build();
        }

        var newUser = User.builder()
                .phoneNumber(user.getPhoneNumber())
                .displayName(user.getDisplayName())
                .about(user.getAbout())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();

        var result = userRepository.save(newUser);

        return UserDto.builder()
                .displayName(result.getDisplayName())
                .phoneNumber(result.getPhoneNumber())
                .about(result.getAbout())
                .profilePictureUrl(result.getProfilePictureUrl())
                .build();
    }

    public UserDto updateUser(UserDto user) throws UsernameNotFoundException {
        var userToUpdate = userRepository.findByPhoneNumber(user.getPhoneNumber())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getDisplayName() != null) {
            userToUpdate.setDisplayName(user.getDisplayName());
        }

        if (user.getAbout() != null) {
            userToUpdate.setAbout(user.getAbout());
        }

        if (user.getProfilePictureUrl() != null) {
            userToUpdate.setProfilePictureUrl(user.getProfilePictureUrl());
        }

        var result = userRepository.save(userToUpdate);

        if (result == null) return null;

        return UserDto.builder()
                .displayName(result.getDisplayName())
                .phoneNumber(result.getPhoneNumber())
                .about(result.getAbout())
                .profilePictureUrl(result.getProfilePictureUrl())
                .build();
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserDto> getAllUsersDto() {
        var users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(
                    UserDto.builder()
                            .displayName(user.getDisplayName())
                            .phoneNumber(user.getPhoneNumber())
                            .about(user.getAbout())
                            .profilePictureUrl(user.getProfilePictureUrl())
                            .build()
            );
        }
        return userDtos;
    }

    public String getCurrentUserPhoneNumber() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public User getCurrentUser() {
        return userRepository.findByPhoneNumber(getCurrentUserPhoneNumber()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDto getCurrentUserDto() {
        var user = userRepository.findByPhoneNumber(getCurrentUserPhoneNumber()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserDto.builder()
                .displayName(user.getDisplayName())
                .phoneNumber(user.getPhoneNumber())
                .about(user.getAbout())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    public List<User> getUsersByPhoneNumber(List<String> phoneNumbers) {
        var users = userRepository.findAllByPhoneNumberIn(phoneNumbers);
        List<User> usersList = new ArrayList<>();
        for (User user : users) {
            usersList.add(user);
        }
        return usersList;
    }

    public List<UserDto> getUsersByPhoneNumbersDto(List<String> phoneNumbers) {
        var users = userRepository.findAllByPhoneNumberIn(phoneNumbers);
        List<UserDto> usersList = new ArrayList<>();
        for (User user : users) {
            usersList.add(
                    UserDto.builder()
                            .displayName(user.getDisplayName())
                            .phoneNumber(user.getPhoneNumber())
                            .about(user.getAbout())
                            .profilePictureUrl(user.getProfilePictureUrl())
                            .build()
            );
        }
        return usersList;
    }
}
