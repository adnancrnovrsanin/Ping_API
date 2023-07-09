package com.adnancrnovrsanin.ping.user.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExistsResponse {
    private UserDto user;
    private boolean userExists;
}
