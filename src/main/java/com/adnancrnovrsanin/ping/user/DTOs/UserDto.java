package com.adnancrnovrsanin.ping.user.DTOs;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String phoneNumber;
    private String displayName;
    private String about;
    private String profilePictureUrl;
}
