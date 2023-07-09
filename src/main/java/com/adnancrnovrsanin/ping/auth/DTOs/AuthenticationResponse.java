package com.adnancrnovrsanin.ping.auth.DTOs;

import com.adnancrnovrsanin.ping.user.DTOs.UserDto;
import com.adnancrnovrsanin.ping.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserDto user;
    private boolean isNewUser;
    private String token;
    private String tokenExpiration;
}
